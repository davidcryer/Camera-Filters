package com.davidcryer.camerafilters.screens.filter;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.UiModel;
import com.davidcryer.camerafilters.framework.opencv.ImageManipulator;

public class FilterUiModel implements UiModel<FilterUi> {
    private ImageManipulator imageManipulator;
    private boolean isMenuOpen;

    public FilterUiModel() {

    }

    private FilterUiModel(final Parcel source) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

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

    ImageManipulator imageManipulator() {
        return imageManipulator;
    }

    void imageManipulator(final ImageManipulator imageManipulator) {
        resetImageManipulator();
        this.imageManipulator = imageManipulator;
    }

    private void resetImageManipulator() {
        if (imageManipulator != null) {
            imageManipulator.release();
        }
    }
}
