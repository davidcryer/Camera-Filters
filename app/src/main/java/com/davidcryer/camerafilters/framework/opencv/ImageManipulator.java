package com.davidcryer.camerafilters.framework.opencv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public class ImageManipulator {

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }

    public void reset() {

    }
}
