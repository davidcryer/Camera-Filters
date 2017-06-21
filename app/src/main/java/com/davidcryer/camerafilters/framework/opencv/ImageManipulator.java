package com.davidcryer.camerafilters.framework.opencv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
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
        processColors(inputFrame.rgba(), inputFrame.gray());
    }

    private void processColors(final Mat rgba, final Mat grey) {
        switch (colorProcessing) {
            default:
            case RGBA: {
                dest = rgba;
            } break;
            case BRGA: {
                Imgproc.cvtColor(rgba, dest, Imgproc.COLOR_RGBA2BGRA, 4);
            } break;
            case GREY: {
                Imgproc.cvtColor(grey, dest, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
            case CANNY: {
                Imgproc.Canny(grey, intermediate, 80, 75);//TODO what are these integers?
                Imgproc.cvtColor(intermediate, dest, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
        }
    }

    private void processImage() {
        processImage(dest, intermediate);
    }

    private void processImage(final Mat dest, final Mat intermediate) {
        switch (imageProcessing) {
            case ZOOM: {
                final int r = dest.rows();
                final int c = dest.cols();
                final Mat zoomCorner = dest.submat(0, r, 0, c);
                final Mat zoomWindow = dest.submat(2 * r / 5, 3 * r / 5, 2 * c / 5, 3 * c / 5);
                final Mat zoomWindowClone = zoomWindow.clone();
                Imgproc.resize(zoomWindowClone, zoomCorner, zoomCorner.size());
                zoomCorner.release();
                zoomWindow.release();
                zoomWindowClone.release();
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

    public byte[] process(final byte[] photo) {
        final Mat pic = Imgcodecs.imdecode(new MatOfByte(photo), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        final Mat grey = pic.clone();
        Imgproc.cvtColor(pic, grey, Imgproc.COLOR_RGB2GRAY);//TODO check type - may need BGR2GRAY
        processColors(pic, grey);
        grey.release();
        final Mat inter = pic.clone();
        processImage(pic, inter);
        inter.release();
        final byte[] bytes = new byte[photo.length];
        pic.get(0, 0, bytes);
        return bytes;
    }


    public void release() {
        intermediate.release();
        dest.release();
    }
}
