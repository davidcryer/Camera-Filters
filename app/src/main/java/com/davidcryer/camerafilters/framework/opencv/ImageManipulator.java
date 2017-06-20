package com.davidcryer.camerafilters.framework.opencv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageManipulator {
    private ColorEffect colorProcessing;
    private ImageEffect imageProcessing;
    private Mat intermediate;
    private Mat dest;

    static {
        System.loadLibrary("MyOpenCVLib");
    }

    public ImageManipulator(final int height, final int width) {
        intermediate = new Mat(height, width, CvType.CV_8SC4);
        dest = new Mat(height, width, CvType.CV_8SC4);
        colorProcessing = ColorEffect.RGBA;
        imageProcessing = ImageEffect.NONE;
    }

    public void colorProcessing(final ColorEffect colorProcessing) {
        this.colorProcessing = colorProcessing;
    }

    public void imageProcessing(final ImageEffect imageProcessing) {
        this.imageProcessing = imageProcessing;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return process(inputFrame);
    }

    private Mat process(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        processColors(inputFrame);
        processImage();
        return dest;
    }

    private void processColors(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        switch (colorProcessing) {
            default:
            case RGBA: {
                dest = inputFrame.rgba();
            } break;
            case BRGA: {
                Imgproc.cvtColor(inputFrame.rgba(), dest, Imgproc.COLOR_RGBA2BGRA, 4);
            } break;
            case GREY: {
                Imgproc.cvtColor(inputFrame.gray(), dest, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
            case CANNY: {
                Imgproc.Canny(inputFrame.gray(), intermediate, 80, 75);//TODO what are these integers?
                Imgproc.cvtColor(intermediate, dest, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
        }
    }

    private void processImage() {
        switch (imageProcessing) {
            case ZOOM: {
                final int r = dest.rows();
                final int c = dest.cols();
                Mat zoomCorner = dest.submat(2 * r / 5, 3 * r / 5, 2 * c / 5, 3 * c / 5);
                Imgproc.resize(dest, zoomCorner, zoomCorner.size());
                zoomCorner.release();
            } break;
            case PIXELISE: {
                Imgproc.resize(dest, intermediate, new Size(), 0.1, 0.1, Imgproc.INTER_NEAREST);
                Imgproc.resize(intermediate, dest, dest.size(), 0., 0., Imgproc.INTER_NEAREST);
            } break;
            case POSTERISE: {
                Imgproc.Canny(dest, intermediate, 80, 90);
                dest.setTo(new Scalar(0, 0, 0, 255), intermediate);
                Core.convertScaleAbs(dest, intermediate, 1. / 16, 0);
                Core.convertScaleAbs(intermediate, dest, 16, 0);
            } break;
        }
    }

    public void release() {
        intermediate.release();
        dest.release();
    }
}
