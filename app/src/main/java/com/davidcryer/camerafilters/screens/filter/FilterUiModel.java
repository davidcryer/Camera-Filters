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
    private ImageEffect imageEffect = ImageEffect.NONE;
    private boolean isMenuOpen = false;
    private boolean isRunning = false;
    private boolean showToggleMenuHint = true;
    private boolean hasAskedForCameraPermission = false;
    private boolean hasAskedForExtWritePermission = false;

    public FilterUiModel() {

    }

    private FilterUiModel(final Parcel source) {
        hasAskedForCameraPermission = source.readByte() != 0;
        hasAskedForExtWritePermission = source.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasAskedForCameraPermission ? 1 : 0));
        dest.writeByte((byte) (hasAskedForExtWritePermission ? 1 : 0));
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
        if (showToggleMenuHint) {
            ui.showToggleMenuHint();
        } else {
            ui.hideToggleMenuHint(false);
        }
    }

    void openMenu(FilterUi ui) {
        if (ui != null) {
            ui.showMenu();
            if (showToggleMenuHint) {
                ui.hideToggleMenuHint(true);
                showToggleMenuHint = false;
            }
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

    ColorEffect colorEffect() {
        return colorEffect;
    }

    void imageEffect(final ImageEffect effect) {
        if (imageManipulator != null) {
            imageManipulator.imageProcessing(effect);
        }
        imageEffect = effect;
    }

    ImageEffect imageEffect() {
        return imageEffect;
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

    void markAskingForCameraPermission() {
        hasAskedForCameraPermission = true;
    }

    boolean hasAskedForCameraPermission() {
        return hasAskedForCameraPermission;
    }

    void markAskingForExtWritePermission() {
        hasAskedForExtWritePermission = true;
    }

    boolean hasAskedForExtWritePermission() {
        return hasAskedForExtWritePermission;
    }
}
