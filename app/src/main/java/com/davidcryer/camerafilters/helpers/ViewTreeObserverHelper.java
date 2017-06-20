package com.davidcryer.camerafilters.helpers;

import android.view.View;
import android.view.ViewTreeObserver;

public class ViewTreeObserverHelper {

    public static void listenForGlobalLayout(final View view, final OnGlobalLayoutListener listener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (listener != null) listener.onLayout();
            }
        });
    }

    public interface OnGlobalLayoutListener {
        void onLayout();
    }
}
