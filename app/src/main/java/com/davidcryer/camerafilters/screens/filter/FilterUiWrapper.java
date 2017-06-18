package com.davidcryer.camerafilters.screens.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.UiWrapper;
import com.davidcryer.camerafilters.framework.opencv.ImageManipulator;
import com.davidcryer.camerafilters.framework.opencv.OpenCvInitialiser;
import com.davidcryer.camerafilters.framework.uiwrapper.UiModelFactory;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public class FilterUiWrapper extends UiWrapper<FilterUi, FilterUi.Listener, FilterUiModel> {

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
                OpenCvInitialiser.init(ui.context(), initCallback);
            }

            @Override
            public void onPause(FilterUi ui) {
                ui.disableSurface();
            }

            @Override
            public void onDestroy(FilterUi ui) {
                ui.disableSurface();
            }

            @Override
            public void onCameraViewStarted(int width, int height, FilterUi ui) {
                uiModel().imageManipulator(new ImageManipulator());
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

    @SuppressWarnings("JniMissingFunction")
    public native String helloWorld();
}
