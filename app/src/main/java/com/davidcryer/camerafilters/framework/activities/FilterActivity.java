package com.davidcryer.camerafilters.framework.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.davidc.uiwrapper.SingleContentContainerWithAppBarActivity;
import com.davidcryer.camerafilters.screens.filter.FilterFragment;

public class FilterActivity extends SingleContentContainerWithAppBarActivity {


    @Override
    protected void setupActionBar(@NonNull ActionBar actionBar) {
        actionBar.hide();
    }

    @NonNull
    @Override
    protected Fragment initialFragment() {
        return new FilterFragment();
    }
}
