package com.davidcryer.camerafilters.screens.filter;

import android.content.Context;

import com.davidc.uiwrapper.Ui;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public interface FilterUi extends Ui {
    void enableSurface();
    void disableSurface();
    Context context();

    interface Listener extends Ui.Listener {
        void onResume(FilterUi ui);
        void onPause(FilterUi ui);
        void onDestroy(FilterUi ui);
        void onCameraViewStarted(int width, int height, FilterUi ui);
        void onCameraViewStopped(FilterUi ui);
        Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame, FilterUi ui);
    }
}
