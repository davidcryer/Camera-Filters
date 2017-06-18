package com.davidcryer.camerafilters.framework.opencv;

import android.content.Context;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

public class OpenCvInitialiser {

    public static void init(final Context context, final Callback callback) {
        if (OpenCVLoader.initDebug()) {
            callback.onInitialised();
        } else {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, context, initCallback(context, callback));
        }
    }

    private static BaseLoaderCallback initCallback(final Context context, final Callback callback) {
        return new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case SUCCESS: {
                        callback.onInitialised();
                    } return;
                }
                super.onManagerConnected(status);
            }
        };
    }

    public interface Callback {
        void onInitialised();
    }
}
