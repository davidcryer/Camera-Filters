package com.davidcryer.camerafilters.screens.filter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.davidcryer.camerafilters.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterMenu extends LinearLayout {
    private Listener listener;
    private boolean isStartCameraFeedState = true;
    @BindView(R.id.effectsContainer)
    View effectsContainer;
    @BindView(R.id.colors)
    Spinner colorSpinner;
    @BindView(R.id.images)
    Spinner imageSpinner;
    @BindView(R.id.onOffToggle)
    Button onOffToggleView;

    public FilterMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_filter_menu, this);
        ButterKnife.bind(this);
        setUpDropdowns();
    }

    private void setUpDropdowns() {
        setUpColorSpinner();
        setUpImageSpinner();
    }

    private void setUpColorSpinner() {
        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_menu_effects_color_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(arrayAdapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener != null) {
                    final String effect = adapterView.getItemAtPosition(i).toString();
                    if (!listener.onSelectColorEffect(effect)) {
                        colorSpinner.setSelection(i, false);
                    } else {
                        ((TextView) colorSpinner.getSelectedView()).setTextColor(Color.WHITE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpImageSpinner() {
        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_menu_effects_image_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(arrayAdapter);
        imageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener != null) {
                    final String effect = adapterView.getItemAtPosition(i).toString();
                    if (!listener.onSelectImageEffect(effect)) {
                        imageSpinner.setSelection(i, false);
                    } else {
                        ((TextView) imageSpinner.getSelectedView()).setTextColor(Color.WHITE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void listener(final Listener listener) {
        this.listener = listener;
    }

    void showStartCameraFeedState() {
        isStartCameraFeedState = true;
        onOffToggleView.setText(getContext().getText(R.string.filter_menu_start));
        effectsContainer.setVisibility(INVISIBLE);
    }

    void showFilterOptionsState() {
        isStartCameraFeedState = false;
        onOffToggleView.setText(getContext().getText(R.string.filter_menu_stop));
        effectsContainer.setVisibility(VISIBLE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.onOffToggle)
    public void onClickOnOffToggle() {
        if (listener != null) listener.onClickOnOffToggle();
    }

    interface Listener {
        boolean onSelectColorEffect(String effect);
        boolean onSelectImageEffect(String effect);
        void onClickOnOffToggle();
    }
}
