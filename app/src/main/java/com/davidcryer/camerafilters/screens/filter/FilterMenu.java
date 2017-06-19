package com.davidcryer.camerafilters.screens.filter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.davidcryer.camerafilters.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterMenu extends LinearLayout {
    private Listener listener;

    public FilterMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_filter_menu, this);
        ButterKnife.bind(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.start)
    public void onClickStart() {
        if (listener != null) listener.onClickStart();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.stop)
    public void onClickStop() {
        if (listener != null) listener.onClickStop();
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

    void listener(final Listener listener) {
        this.listener = listener;
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
