package com.davidcryer.camerafilters.screens.filter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.Ui;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public interface FilterUi extends Ui {
    void showPermissionNotGrantedDialog();
    void enableSurface();
    void disableSurface();
    void showMenu();
    void hideMenu();
    Activity activity();

    interface Listener extends Ui.Listener {
        void onResume(FilterUi ui);
        void onPause(FilterUi ui);
        void onDestroy(FilterUi ui);
        void onPermissionsReturned(FilterUi ui, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
        void onPermissionNotGrantedDialogDismissed(FilterUi ui);
        void onCameraViewStarted(int width, int height, FilterUi ui);
        void onCameraViewStopped(FilterUi ui);
        boolean onBackPressed(FilterUi ui);
        void onClickRoot(FilterUi ui);
        void onClickOnOffToggle(FilterUi ui);
        boolean onClickMenuColorProcessing(FilterUi ui, String effect);
        boolean onClickMenuImageProcessing(FilterUi ui, String effect);
        Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame, FilterUi ui);
    }
}
