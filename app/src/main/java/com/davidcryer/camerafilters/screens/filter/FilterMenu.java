package com.davidcryer.camerafilters.screens.filter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.davidcryer.camerafilters.R;
import com.davidcryer.camerafilters.helpers.AnimationHelper;
import com.davidcryer.camerafilters.helpers.SimpleAnimationCallback;
import com.davidcryer.camerafilters.helpers.ViewTreeObserverHelper;

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
    @BindView(R.id.take_photo)
    View takePhotoView;
    private int selectedColorIndex;
    private int selectedImageIndex;
    private boolean hasSetUp = false;

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
                    if (listener.onSelectColorEffect(effect)) {
                        colorSelectedTextWhite(colorSpinner);
                        selectedColorIndex = i;
                        return;
                    }
                }
                colorSpinner.setSelection(selectedColorIndex, false);
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
                    if (listener.onSelectImageEffect(effect)) {
                        colorSelectedTextWhite(imageSpinner);
                        selectedImageIndex = i;
                        return;
                    }
                }
                imageSpinner.setSelection(selectedImageIndex, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private static void colorSelectedTextWhite(final Spinner spinner) {
        final TextView selectedTextView = ((TextView) spinner.getSelectedView());
        if (selectedTextView != null) {
            selectedTextView.setTextColor(Color.WHITE);
        }
    }

    void listener(final Listener listener) {
        this.listener = listener;
    }

    void showStartCameraFeedState() {
        isStartCameraFeedState = true;
        onOffToggleView.setText(getContext().getText(R.string.filter_menu_start));
        AnimationHelper.fade(takePhotoView, takePhotoView.getAlpha(), 0f, 300L, new SimpleAnimationCallback() {
            @Override
            public void onStart() {
                takePhotoView.setOnClickListener(null);
            }
        });
        AnimationHelper.x(effectsContainer, effectsContainer.getX(), -effectsContainer.getWidth(), 400, new AccelerateInterpolator(), null);
    }

    void showFilterOptionsState() {
        isStartCameraFeedState = false;
        colorSelectedTextWhite(colorSpinner);
        colorSelectedTextWhite(imageSpinner);
        onOffToggleView.setText(getContext().getText(R.string.filter_menu_stop));
        doTakePhotoIconAnimateIn();
        AnimationHelper.x(effectsContainer, effectsContainer.getX(), 0, 400, new DecelerateInterpolator(), null);
    }

    private void doTakePhotoIconAnimateIn() {
        if (takePhotoView.getWidth() == 0) {
            takePhotoView.setVisibility(VISIBLE);
            ViewTreeObserverHelper.listenForGlobalLayout(takePhotoView, new ViewTreeObserverHelper.OnGlobalLayoutListener() {
                @Override
                public void onLayout() {
                    animateInTakePhotoIcon();
                }
            });
        } else {
            animateInTakePhotoIcon();
        }
    }

    private void animateInTakePhotoIcon() {
        AnimationHelper.fade(takePhotoView, takePhotoView.getAlpha(), 1f, 300L, new SimpleAnimationCallback() {
            @Override
            public void onFinish() {
                takePhotoView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickTakePhoto();
                    }
                });
            }
        });
    }

    void setUpIfAppropriate() {
        if (!hasSetUp) {
            ViewTreeObserverHelper.listenForGlobalLayout(effectsContainer, new ViewTreeObserverHelper.OnGlobalLayoutListener() {
                @Override
                public void onLayout() {
                    setUpEffectsContainer();
                }
            });
            hasSetUp = true;
        }
    }

    private void setUpEffectsContainer() {
        if (isStartCameraFeedState) {
            effectsContainer.setX(-effectsContainer.getWidth());
        } else {
            effectsContainer.setX(0f);
        }
    }

    private void onClickTakePhoto() {
        if (listener != null) listener.onClickTakePhoto();
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
        void onClickTakePhoto();
    }
}
