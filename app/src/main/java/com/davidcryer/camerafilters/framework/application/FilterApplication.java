package com.davidcryer.camerafilters.framework.application;

import android.app.Application;

import com.davidc.uiwrapper.UiWrapperFactoryProvider;
import com.davidcryer.camerafilters.framework.uiwrapper.UiWrapperFactory;

public class FilterApplication extends Application implements UiWrapperFactoryProvider<UiWrapperFactory> {
    private UiWrapperFactory uiWrapperFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        uiWrapperFactory = new UiWrapperFactory();
    }

    @Override
    public UiWrapperFactory getUiWrapperFactory() {
        return uiWrapperFactory;
    }
}
