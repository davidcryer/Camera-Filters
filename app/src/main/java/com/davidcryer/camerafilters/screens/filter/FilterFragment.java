package com.davidcryer.camerafilters.screens.filter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.davidc.uiwrapper.UiFragment;
import com.davidcryer.camerafilters.R;
import com.davidcryer.camerafilters.framework.uiwrapper.UiWrapperRepository;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FilterFragment extends UiFragment<UiWrapperRepository, FilterUi.Listener> {
    private Unbinder unbinder;
    @BindView(R.id.surface)
    CameraBridgeViewBase surfaceView;

    @Override
    protected FilterUi.Listener bind(@NonNull UiWrapperRepository uiWrapperRepository, @NonNull String instanceId, @Nullable Bundle savedInstanceState) {
        return uiWrapperRepository.bind(ui, instanceId, savedInstanceState);
    }

    @Override
    protected void unbind(@NonNull UiWrapperRepository uiWrapperRepository, @NonNull String instanceId, @Nullable Bundle outState, boolean isConfigurationChange) {
        uiWrapperRepository.unbind(ui, instanceId, outState, isConfigurationChange);
    }

    private final FilterUi ui = new FilterUi() {
        @Override
        public void enableSurface() {
            surfaceView.enableView();
        }

        @Override
        public void disableSurface() {
            surfaceView.disableView();
        }

        @Override
        public Context context() {
            return getActivity();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSurfaceView();
    }

    private void setUpSurfaceView() {
        surfaceView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                if (hasListener()) listener().onCameraViewStarted(width, height, ui);
            }

            @Override
            public void onCameraViewStopped() {
                if (hasListener()) listener().onCameraViewStopped(ui);
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                return hasListener() ? listener().onCameraFrame(inputFrame, ui) : null;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasListener()) listener().onResume(ui);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hasListener()) listener().onPause(ui);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasListener()) listener().onDestroy(ui);
    }
}
