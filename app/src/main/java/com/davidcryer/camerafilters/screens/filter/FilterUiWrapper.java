package com.davidcryer.camerafilters.screens.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.UiWrapper;
import com.davidcryer.camerafilters.framework.uiwrapper.UiModelFactory;

public class FilterUiWrapper extends UiWrapper<FilterUi, FilterUi.Listener, FilterUiModel> {

    static {
        System.loadLibrary("MyLib");
    }

    private FilterUiWrapper(@NonNull FilterUiModel uiModel) {
        super(uiModel);
    }

    public static FilterUiWrapper newInstance() {
        return new FilterUiWrapper(UiModelFactory.createFilterUiModel());
    }

    public static FilterUiWrapper savedElseNewInstance(final Bundle savedInstanceState) {
        final FilterUiModel model = savedUiModel(savedInstanceState);
        return model == null ? newInstance() : new FilterUiWrapper(model);
    }

    @Override
    protected FilterUi.Listener uiListener() {
        return new FilterUi.Listener() {

        };
    }

    @SuppressWarnings("JniMissingFunction")
    public native String helloWorld();
}
