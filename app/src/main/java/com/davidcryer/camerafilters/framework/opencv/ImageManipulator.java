package com.davidcryer.camerafilters.framework.opencv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageManipulator {
    public enum ColorProcessing {RGBA, BRGA, GREY, CANNY}
    private ColorProcessing colorProcessing;
    private Mat intermediate;
    private Mat dest;

    static {
        System.loadLibrary("MyOpenCVLib");
    }

    public ImageManipulator(final int height, final int width) {
        intermediate = new Mat(height, width, CvType.CV_8SC4);
        dest = new Mat(height, width, CvType.CV_8SC4);
        colorProcessing = ColorProcessing.RGBA;
    }

    public void colorProcessing(final ColorProcessing colorProcessing) {
        this.colorProcessing = colorProcessing;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return process(inputFrame);
    }

    private Mat process(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        processColors(inputFrame);
        return dest;
    }

    private void processColors(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        switch (colorProcessing) {
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

    public void release() {
        intermediate.release();
        dest.release();
    }
}
