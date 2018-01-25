package com.davidcryer.camerafilters.screens.filter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.davidc.uiwrapper.UiWrapper;
import com.davidc.uiwrapper.UiWrapperFactoryFragment;
import com.davidcryer.camerafilters.R;
import com.davidcryer.camerafilters.framework.activities.OnBackPressedNotifier;
import com.davidcryer.camerafilters.framework.opencv.UiLessJavaCameraView;
import com.davidcryer.camerafilters.framework.uiwrapper.UiWrapperFactory;
import com.davidcryer.camerafilters.helpers.AnimationHelper;
import com.davidcryer.camerafilters.helpers.ViewTreeObserverHelper;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterFragment extends UiWrapperFactoryFragment<FilterUi, FilterUi.Listener, UiWrapperFactory> {
    private Unbinder unbinder;
    @BindView(R.id.surface)
    UiLessJavaCameraView surfaceView;
    @BindView(R.id.menu)
    FilterMenu menuView;
    @BindView(R.id.orig)
    SurfaceView origView;
    @BindView(R.id.mod)
    SurfaceView modView;
    @BindView(R.id.menu_toggle_hint)
    View menuToggleHintView;
    View root;

    private final FilterUi ui = new FilterUi() {
        @Override
        public void showToggleMenuHint() {
            AnimationHelper.fade(menuToggleHintView, menuToggleHintView.getAlpha(), 1f, 200, null);
        }

        @Override
        public void hideToggleMenuHint(boolean animate) {
            if (animate) {
                AnimationHelper.fade(menuToggleHintView, menuToggleHintView.getAlpha(), 0f, 200, null);
            } else {
                menuToggleHintView.setAlpha(0f);
            }
        }

        @Override
        public void enableSurface() {
            surfaceView.registerForOriginalFrames(origView.getHolder());
            surfaceView.registerForModifiedFrames(modView.getHolder());
            surfaceView.enableView();
            menuView.showFilterOptionsState();
        }

        @Override
        public void disableSurface() {
            surfaceView.unRegisterForOriginalFrames(origView.getHolder());
            surfaceView.unRegisterForModifiedFrames(modView.getHolder());
            surfaceView.disableView();
            menuView.showStartCameraFeedState();
        }

        @Override
        public void showMenu() {
            menuView.setUpIfAppropriate();
            doMenuAnimationIn();
        }

        @Override
        public void hideMenu() {
            AnimationHelper.y(menuView, menuView.getY(), root.getHeight(), 400, new AccelerateInterpolator(), null);
        }

        @Override
        public void takePhotograph() {
            surfaceView.takePicture(new UiLessJavaCameraView.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] bytes, int height, int width) {
                    listener().onPictureTaken(ui, bytes, height, width);
                }
            });
        }

        @Override
        public void showToast(String text) {
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public Fragment fragment() {
            return FilterFragment.this;
        }

        @Override
        public Activity activity() {
            return getActivity();
        }
    };

    private void doMenuAnimationIn() {
        if (menuView.getHeight() == 0) {
            menuView.setVisibility(View.VISIBLE);
            ViewTreeObserverHelper.listenForGlobalLayout(menuView, new ViewTreeObserverHelper.OnGlobalLayoutListener() {
                @Override
                public void onLayout() {
                    menuView.setY(root.getHeight());
                    animateInMenu();
                }
            });
        } else {
            animateInMenu();
        }
    }

    private void animateInMenu() {
        AnimationHelper.y(menuView, menuView.getY(), root.getHeight() - menuView.getHeight(), 400, new DecelerateInterpolator(), null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, root);
        setCameraViewSize();
        return root;
    }

    private void setCameraViewSize() {
        origView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                origView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                surfaceView.getLayoutParams().height = origView.getHeight();
                surfaceView.getLayoutParams().width = origView.getWidth();
            }
        });
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
            FilterUi.Listener listener() {
                return FilterFragment.this.listener();
            }
        });
    }

    private void setUpSurfaceView() {
        surfaceView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                listener().onCameraViewStarted(width, height, ui);
            }

            @Override
            public void onCameraViewStopped() {
                listener().onCameraViewStopped(ui);
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                return listener().onCameraFrame(inputFrame, ui);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        listener().onStart(ui);
    }

    @Override
    public void onStop() {
        super.onStop();
        listener().onStop(ui);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((OnBackPressedNotifier) getActivity()).register(onBackPressedListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        ((OnBackPressedNotifier) getActivity()).unRegister(onBackPressedListener);
    }

    @Override
    public void onDestroy() {
        listener().onDestroy(ui);
        super.onDestroy();
    }

    private final OnBackPressedNotifier.Listener onBackPressedListener = new OnBackPressedNotifier.Listener() {
        @Override
        public boolean onBackPressed() {
            return listener().onBackPressed(ui);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        listener().onPermissionsReturned(ui, requestCode, permissions, grantResults);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.menu_toggle)
    public void onClickMenuToggle() {
        listener().onClickMenuToggle(ui);
    }

    @Override
    protected UiWrapper<FilterUi, FilterUi.Listener, ?> uiWrapper(UiWrapperFactory uiWrapperFactory, @Nullable Bundle savedState) {
        return uiWrapperFactory.filterUiWrapper(savedState);
    }

    @Override
    protected FilterUi ui() {
        return ui;
    }
}
