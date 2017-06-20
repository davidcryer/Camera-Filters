package com.davidcryer.camerafilters.screens.filter;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.UiModel;
import com.davidcryer.camerafilters.framework.opencv.ColorEffect;
import com.davidcryer.camerafilters.framework.opencv.ImageEffect;
import com.davidcryer.camerafilters.framework.opencv.ImageManipulator;

public class FilterUiModel implements UiModel<FilterUi> {
    private ImageManipulator imageManipulator;
    private ColorEffect colorEffect = ColorEffect.RGBA;
    private ImageEffect imageEffect= ImageEffect.NONE;
    private boolean isMenuOpen = false;
    private boolean isRunning = false;

    public FilterUiModel() {

    }

    private FilterUiModel(final Parcel source) {
        colorEffect = (ColorEffect) source.readSerializable();
        imageEffect = (ImageEffect) source.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(colorEffect);
        dest.writeSerializable(imageEffect);
    }

    public final static Creator<FilterUiModel> CREATOR = new Creator<FilterUiModel>() {
        @Override
        public FilterUiModel createFromParcel(Parcel source) {
            return new FilterUiModel(source);
        }

        @Override
        public FilterUiModel[] newArray(int size) {
            return new FilterUiModel[size];
        }
    };

    @Override
    public void onto(@NonNull FilterUi ui) {
        isMenuOpen = false;
        isRunning = false;
    }

    void openMenu(FilterUi ui) {
        if (ui != null) {
            ui.showMenu();
        }
        isMenuOpen = true;
    }

    void closeMenu(FilterUi ui) {
        if (ui != null) {
            ui.hideMenu();
        }
        isMenuOpen = false;
    }

    boolean isMenuOpen() {
        return isMenuOpen;
    }

    void toggleRunningState(FilterUi ui) {
        if (ui != null) {
            if (isRunning) {
                ui.disableSurface();
            } else {
                ui.enableSurface();
            }
        }
        isRunning = !isRunning;
    }

    void colorEffect(final ColorEffect effect) {
        if (imageManipulator != null) {
            imageManipulator.colorProcessing(effect);
        }
        colorEffect = effect;
    }

    void imageEffect(final ImageEffect effect) {
        if (imageManipulator != null) {
            imageManipulator.imageProcessing(effect);
        }
        imageEffect = effect;
    }

    ImageManipulator imageManipulator() {
        return imageManipulator;
    }

    void imageManipulator(final ImageManipulator imageManipulator) {
        resetImageManipulator();
        this.imageManipulator = imageManipulator;
        if (imageManipulator != null) {
            imageManipulator.colorProcessing(colorEffect);
            imageManipulator.imageProcessing(imageEffect);
        }
    }

    private void resetImageManipulator() {
        if (imageManipulator != null) {
            imageManipulator.release();
        }
    }
}
