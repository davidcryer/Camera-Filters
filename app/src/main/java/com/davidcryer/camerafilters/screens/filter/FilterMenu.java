package com.davidcryer.camerafilters.screens.filter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidcryer.camerafilters.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterMenu extends LinearLayout {
    private Listener listener;
    private boolean isStartCameraFeedState = true;
    @BindView(R.id.onOffToggle)
    TextView onOffToggleView;

    public FilterMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_filter_menu, this);
        ButterKnife.bind(this);
    }

    void listener(final Listener listener) {
        this.listener = listener;
    }

    void showStartCameraFeedState() {
        isStartCameraFeedState = true;
        onOffToggleView.setText(getContext().getText(R.string.filter_menu_start));
    }

    void showFilterOptionsState() {
        isStartCameraFeedState = false;
        onOffToggleView.setText(getContext().getText(R.string.filter_menu_stop));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.onOffToggle)
    public void onClickStart() {
        if (listener != null) {
            if (isStartCameraFeedState) {
                listener.onClickStart();
            } else {
                listener.onClickStop();
            }
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.rgba)
    public void onClickRgba() {
        if (listener != null) listener.onClickRgba();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.brga)
    public void onClickBrga() {
        if (listener != null) listener.onClickBrga();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.grey)
    public void onClickGrey() {
        if (listener != null) listener.onClickGrey();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.canny)
    public void onClickCanny() {
        if (listener != null) listener.onClickCanny();
    }

    interface Listener {
        void onClickStart();
        void onClickStop();
        void onClickRgba();
        void onClickBrga();
        void onClickGrey();
        void onClickCanny();
    }
}
