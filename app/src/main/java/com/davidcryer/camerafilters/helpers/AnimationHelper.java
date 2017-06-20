package com.davidcryer.camerafilters.helpers;

import android.view.View;
import android.view.animation.Interpolator;

public class AnimationHelper {

    public static void x(final View v, final float st, final float fin, final long maxDur, final Interpolator interpolator, final SimpleAnimationCallback callback) {
        SimpleAnimationCallback.onStart(callback);
        v.animate()
                .x(fin)
                .setDuration(duration(st, fin, v.getX(), maxDur))
                .setInterpolator(interpolator)
                .withEndAction(SimpleAnimationCallback.endAction(callback))
                .start();
    }

    public static void y(final View v, final float st, final float fin, final long maxDur, final Interpolator interpolator, final SimpleAnimationCallback callback) {
        SimpleAnimationCallback.onStart(callback);
        v.animate()
                .y(fin)
                .setDuration(duration(st, fin, v.getY(), maxDur))
                .setInterpolator(interpolator)
                .withEndAction(SimpleAnimationCallback.endAction(callback))
                .start();
    }

    private static long duration(final float st, final float fin, final float cur, final long max) {
        final float extreme = fin - st;
        final float remaining = fin - cur;
        return max * (long) (remaining / extreme);
    }
}
