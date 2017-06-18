package com.davidcryer.camerafilters.framework.opencv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageManipulator {
    private Mat rgba;

    public ImageManipulator(final int height, final int width) {
        this.rgba = new Mat(height, width, CvType.CV_8SC4);
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Imgproc.cvtColor(inputFrame.gray(), rgba, Imgproc.COLOR_GRAY2RGBA, 4);
        return rgba;
    }

    public void release() {
        rgba.release();
    }
}
