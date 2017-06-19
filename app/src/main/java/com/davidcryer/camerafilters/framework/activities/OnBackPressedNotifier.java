package com.davidcryer.camerafilters.framework.activities;

public interface OnBackPressedNotifier {
    void register(Listener listener);
    void unRegister(Listener listener);

    interface Listener {
        boolean onBackPressed();
    }
}
