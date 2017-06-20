package com.davidcryer.camerafilters.framework.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.WindowManager;

import com.davidc.uiwrapper.SingleContentContainerWithAppBarActivity;
import com.davidcryer.camerafilters.screens.filter.FilterFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FilterActivity extends SingleContentContainerWithAppBarActivity implements OnBackPressedNotifier {
    private final LinkedList<Listener> onBackPressedListeners = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActionBar(@NonNull ActionBar actionBar) {
        actionBar.hide();
    }

    @NonNull
    @Override
    protected Fragment initialFragment() {
        return new FilterFragment();
    }

    @Override
    public void onBackPressed() {
        if (consumeOnBackPressedByListeners()) {
            return;
        }
        super.onBackPressed();
    }

    private boolean consumeOnBackPressedByListeners() {
        for (final OnBackPressedNotifier.Listener listener : onBackPressedListeners) {
            if (listener.onBackPressed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void register(Listener listener) {
        if (!onBackPressedListeners.contains(listener)) {
            onBackPressedListeners.add(0, listener);
        }
    }

    @Override
    public void unRegister(Listener listener) {
        onBackPressedListeners.remove(listener);
    }
}
