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
        dest = inputFrame.rgba();
        final int r = dest.rows();
        final int c = dest.cols();

        Mat rgbaFilterWindow = dest.submat(r / 4, (3 * r) / 4, c / 4, (3 * c) / 4);
        Mat rgbaFilterWindowClone = rgbaFilterWindow.clone();
        Mat greyFilterWindow = inputFrame.gray().submat(r / 4, (3 * r) / 4, c / 4, (3 * c) / 4);
        Mat rgbaInnerWindow = dest.submat(r / 4, (3 * r) / 4, 0, c / 2);
        processColors(rgbaInnerWindow, rgbaFilterWindowClone, greyFilterWindow);
        rgbaFilterWindow.release();
        rgbaFilterWindowClone.release();
        greyFilterWindow.release();
        rgbaInnerWindow.release();
        return dest;
    }

    private Mat processColors(final Mat target, final Mat rgbaFilter, final Mat greyFilter) {
        switch (colorProcessing) {
            case RGBA: {

            } break;
            case BRGA: {
                Imgproc.cvtColor(rgbaFilter, rgbaFilter, Imgproc.COLOR_RGBA2BGRA, 4);
            } break;
            case GREY: {
                Imgproc.cvtColor(greyFilter, rgbaFilter, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
            case CANNY: {
                Imgproc.Canny(greyFilter, intermediate, 80, 100);//TODO what are these integers?
                Imgproc.cvtColor(intermediate, rgbaFilter, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
        }
        Imgproc.resize(rgbaFilter, target, target.size());
        return target;
    }

    public void release() {
        intermediate.release();
        dest.release();
    }
}
