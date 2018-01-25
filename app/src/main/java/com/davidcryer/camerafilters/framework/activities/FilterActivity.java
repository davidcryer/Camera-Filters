package com.davidcryer.camerafilters.framework.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.WindowManager;

import com.davidcryer.camerafilters.screens.filter.FilterFragment;
import com.davidcryer.simpleactivities.SimpleAppBarActivity;

import java.util.LinkedList;

public class FilterActivity extends SimpleAppBarActivity implements OnBackPressedNotifier {
    private final static String FRAGMENT_FILTER = "filter";
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

    @Override
    protected void addInitialFragment() {
        add(FRAGMENT_FILTER, FilterFragment::new);
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
