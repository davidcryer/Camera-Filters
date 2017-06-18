package com.davidcryer.camerafilters.framework.uiwrapper;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.davidcryer.camerafilters.screens.filter.FilterUiWrapper;

public class UiWrapperFactory {

    public static FilterUiWrapper createFilterUiWrapper(@Nullable final Bundle savedInstanceState) {
        return savedInstanceState == null ? FilterUiWrapper.newInstance() : FilterUiWrapper.savedElseNewInstance(savedInstanceState);
    }
}
