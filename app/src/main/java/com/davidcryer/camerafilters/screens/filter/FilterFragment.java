package com.davidcryer.camerafilters.screens.filter;

import android.app.Activity;
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
import com.davidcryer.camerafilters.framework.activities.OnBackPressedNotifier;
import com.davidcryer.camerafilters.framework.uiwrapper.UiWrapperRepository;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterFragment extends UiFragment<UiWrapperRepository, FilterUi.Listener> {
    private Unbinder unbinder;
    @BindView(R.id.surface)
    CameraBridgeViewBase surfaceView;
    @BindView(R.id.menu)
    FilterMenu menuView;

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
        public void showPermissionNotGrantedDialog() {
            //TODO
        }

        @Override
        public void enableSurface() {
            surfaceView.enableView();
        }

        @Override
        public void disableSurface() {
            surfaceView.disableView();
        }

        @Override
        public void showMenu() {
            //TODO animation for showing menu
        }

        @Override
        public void hideMenu() {
            //TODO animation for hiding menu
        }

        @Override
        public Activity activity() {
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
        setUpFilterMenu();
        setUpSurfaceView();
    }

    private void setUpFilterMenu() {
        menuView.listener(new FilterMenuListenerForFilterUiListenerAdapter(ui) {
            @Override
            boolean hasListener() {
                return FilterFragment.this.hasListener();
            }

            @Override
            FilterUi.Listener listener() {
                return FilterFragment.this.listener();
            }
        });
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
        ((OnBackPressedNotifier) getActivity()).register(onBackPressedListener);
        if (hasListener()) listener().onResume(ui);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((OnBackPressedNotifier) getActivity()).unRegister(onBackPressedListener);
        if (hasListener()) listener().onPause(ui);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasListener()) listener().onDestroy(ui);
    }

    private final OnBackPressedNotifier.Listener onBackPressedListener = new OnBackPressedNotifier.Listener() {
        @Override
        public boolean onBackPressed() {
            return hasListener() && listener().onBackPressed(ui);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasListener()) listener().onPermissionsReturned(ui, requestCode, permissions, grantResults);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.surface)
    public void onClickSurface() {
        if (hasListener()) listener().onClickSurface(ui);
    }
}
