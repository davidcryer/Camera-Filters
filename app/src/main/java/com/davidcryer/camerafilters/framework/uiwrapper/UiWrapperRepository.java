package com.davidcryer.camerafilters.framework.uiwrapper;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.BaseUiWrapperRepository;
import com.davidc.uiwrapper.UiWrapper;
import com.davidcryer.camerafilters.screens.filter.FilterUi;
import com.davidcryer.camerafilters.screens.filter.FilterUiModel;

import java.util.HashMap;
import java.util.Map;

public class UiWrapperRepository extends BaseUiWrapperRepository {
    private final Map<String, UiWrapper<FilterUi, FilterUi.Listener, FilterUiModel>> filterUiWrapperMap = new HashMap<>();

    public FilterUi.Listener bind(final FilterUi ui, final String instanceId, final Bundle savedInstanceState) {
        return bind(ui, instanceId, filterUiWrapperMap, new UiWrapperProvider<FilterUi, FilterUi.Listener, FilterUiModel>() {
            @NonNull
            @Override
            public UiWrapper<FilterUi, FilterUi.Listener, FilterUiModel> uiWrapper() {
                return UiWrapperFactory.createFilterUiWrapper(savedInstanceState);
            }
        });
    }

    public void unbind(final FilterUi ui, final String instanceId, final Bundle outState, final boolean isConfigurationChange) {
        unbind(instanceId, filterUiWrapperMap, outState, isConfigurationChange);
    }
}
