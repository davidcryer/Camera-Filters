package com.davidcryer.camerafilters.helpers;

public class SimpleAnimationCallback {
    public void onStart() {}
    public void onFinish() {}

    static void onStart(final SimpleAnimationCallback callback) {
        if (callback != null) callback.onStart();
    }

    static Runnable endAction(final SimpleAnimationCallback callback) {
        if (callback == null) {
            return null;
        }
        return new Runnable() {
            @Override
            public void run() {
                callback.onFinish();
            }
        };
    }
}
