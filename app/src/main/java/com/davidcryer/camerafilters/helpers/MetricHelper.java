package com.davidcryer.camerafilters.helpers;

public class MetricHelper {

    public static float toPix(final float dp, final float scale) {
        return dp * scale + 0.5f;
    }
}
