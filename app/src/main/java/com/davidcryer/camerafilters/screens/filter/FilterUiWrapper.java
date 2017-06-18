package com.davidcryer.camerafilters.screens.filter;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.UiWrapper;
import com.davidcryer.camerafilters.framework.opencv.ImageManipulator;
import com.davidcryer.camerafilters.framework.opencv.OpenCvInitialiser;
import com.davidcryer.camerafilters.framework.uiwrapper.UiModelFactory;
import com.davidcryer.camerafilters.helpers.PermissionHelper;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public class FilterUiWrapper extends UiWrapper<FilterUi, FilterUi.Listener, FilterUiModel> {
    private final static int PERMISSION_REQUEST_CAMERA = 1;

    private FilterUiWrapper(@NonNull FilterUiModel uiModel) {
        super(uiModel);
    }

    public static FilterUiWrapper newInstance() {
        return new FilterUiWrapper(UiModelFactory.createFilterUiModel());
    }

    public static FilterUiWrapper savedElseNewInstance(final Bundle savedInstanceState) {
        final FilterUiModel model = savedUiModel(savedInstanceState);
        return model == null ? newInstance() : new FilterUiWrapper(model);
    }

    @Override
    protected FilterUi.Listener uiListener() {
        return new FilterUi.Listener() {
            @Override
            public void onResume(FilterUi ui) {
                getCameraPermission(ui);
            }

            @Override
            public void onPause(FilterUi ui) {
                ui.disableSurface();
            }

            @Override
            public void onPermissionsReturned(FilterUi ui, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                processReturnedPermissions(ui, requestCode, grantResults);
            }

            @Override
            public void onPermissionNotGrantedDialogDismissed(FilterUi ui) {
                getCameraPermission(ui);
            }

            @Override
            public void onDestroy(FilterUi ui) {
                ui.disableSurface();
            }

            @Override
            public void onCameraViewStarted(int width, int height, FilterUi ui) {
                uiModel().imageManipulator(new ImageManipulator(height, width));
            }

            @Override
            public void onCameraViewStopped(FilterUi ui) {
                uiModel().imageManipulator(null);
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame, FilterUi ui) {
                return uiModel().imageManipulator().onCameraFrame(inputFrame);
            }
        };
    }

    private void getCameraPermission(final FilterUi ui) {
        if (PermissionHelper.has(Manifest.permission.CAMERA, ui.activity())) {
            initOpenCvLibrary(ui);
        } else {
            requestCameraPermission(ui);
        }
    }

    private void initOpenCvLibrary(final FilterUi ui) {
        OpenCvInitialiser.init(ui.activity(), initCallback);
    }

    private void requestCameraPermission(FilterUi ui) {
        PermissionHelper.request(Manifest.permission.CAMERA, PERMISSION_REQUEST_CAMERA, ui.activity());
    }

    private void processReturnedPermissions(final FilterUi ui, final int requestCode, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                if (PermissionHelper.isGranted(grantResults[0])) {
                    initOpenCvLibrary(ui);
                } else {
                    ui.showPermissionNotGrantedDialog();
                }
            } break;
        }
    }

    private final OpenCvInitialiser.Callback initCallback = new OpenCvInitialiser.Callback() {
        @Override
        public void onInitialised() {
            System.loadLibrary("MyOpenCVLib");
            final FilterUi ui = ui();
            if (ui != null) {
                ui.enableSurface();
            }
        }
    };
}
