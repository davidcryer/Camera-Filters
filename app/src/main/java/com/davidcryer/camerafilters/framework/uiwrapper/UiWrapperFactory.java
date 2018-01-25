package com.davidcryer.camerafilters.framework.uiwrapper;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.davidc.uiwrapper.UiWrapperInitializer;
import com.davidcryer.camerafilters.screens.filter.FilterUiWrapper;

public class UiWrapperFactory {

    public FilterUiWrapper filterUiWrapper(@Nullable final Bundle savedState) {
        return UiWrapperInitializer.from(savedState, FilterUiWrapper::newInstance, FilterUiWrapper::savedState);
    }
}
