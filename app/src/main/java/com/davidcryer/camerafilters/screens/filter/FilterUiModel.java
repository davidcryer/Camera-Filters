package com.davidcryer.camerafilters.screens.filter;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.davidc.uiwrapper.UiModel;

public class FilterUiModel implements UiModel<FilterUi> {


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
}
