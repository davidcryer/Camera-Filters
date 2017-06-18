package com.davidcryer.camerafilters.framework.application;

import android.app.Application;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.BaseUiWrapperRepository;
import com.davidc.uiwrapper.UiWrapperRepositoryFactory;
import com.davidcryer.camerafilters.framework.uiwrapper.UiWrapperRepository;

public class FilterApplication extends Application implements UiWrapperRepositoryFactory {

    @NonNull
    @Override
    public BaseUiWrapperRepository create() {
        return new UiWrapperRepository();
    }
}
