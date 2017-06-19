package com.davidcryer.camerafilters.framework.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import org.opencv.BuildConfig;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.HashSet;
import java.util.Set;

public class UiLessJavaCameraView extends JavaCameraView {
    private Set<SurfaceHolder> origHolders = new HashSet<>();
    private Set<SurfaceHolder> modHolders = new HashSet<>();
    private Bitmap origCachedBitmap;

    public UiLessJavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void registerForOriginalFrames(final SurfaceHolder holder) {
        origHolders.add(holder);
    }

    public void unRegisterForOriginalFrames(final SurfaceHolder holder) {
        origHolders.remove(holder);
    }

    public void registerForModifiedFrames(final SurfaceHolder holder) {
        modHolders.add(holder);
    }

    public void unRegisterForModifiedFrames(final SurfaceHolder holder) {
        modHolders.remove(holder);
    }

    @Override
    protected void deliverAndDrawFrame(CvCameraViewFrame frame) {
        boolean bmpValid = true;
        final Mat unmodified = frame.rgba();
        if (unmodified != null) {
            try {
                Utils.matToBitmap(unmodified, origCachedBitmap);
            } catch(Exception e) {
                Log.e(getClass().getSimpleName(), "Mat type: " + unmodified);
                Log.e(getClass().getSimpleName(), "Bitmap type: " + origCachedBitmap.getWidth() + "*" + origCachedBitmap.getHeight());
                Log.e(getClass().getSimpleName(), "Utils.matToBitmap() throws an exception: " + e.getMessage());
                bmpValid = false;
            }
        }

        final Mat modified = mListener != null ? mListener.onCameraFrame(frame) : frame.rgba();
        if (modified != null) {
            try {
                Utils.matToBitmap(modified, mCacheBitmap);
            } catch(Exception e) {
                Log.e(getClass().getSimpleName(), "Mat type: " + modified);
                Log.e(getClass().getSimpleName(), "Bitmap type: " + mCacheBitmap.getWidth() + "*" + mCacheBitmap.getHeight());
                Log.e(getClass().getSimpleName(), "Utils.matToBitmap() throws an exception: " + e.getMessage());
                bmpValid = false;
            }
        }

        if (bmpValid) {
            onto(mCacheBitmap, modHolders);
            onto(origCachedBitmap, origHolders);
        }
    }

    private void onto(final Bitmap bitmap, final Set<SurfaceHolder> holders) {
        if (bitmap != null) {
            for (final SurfaceHolder holder : holders) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    onto(bitmap, canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void onto(final Bitmap bitmap, final Canvas canvas) {
        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        if (BuildConfig.DEBUG)
            Log.d(getClass().getSimpleName(), "mStretch value: " + mScale);

        if (mScale != 0) {
            canvas.drawBitmap(bitmap, new Rect(0,0, bitmap.getWidth(), bitmap.getHeight()),
                    new Rect((int)((canvas.getWidth() - mScale* bitmap.getWidth()) / 2),
                            (int)((canvas.getHeight() - mScale* bitmap.getHeight()) / 2),
                            (int)((canvas.getWidth() - mScale* bitmap.getWidth()) / 2 + mScale* bitmap.getWidth()),
                            (int)((canvas.getHeight() - mScale* bitmap.getHeight()) / 2 + mScale* bitmap.getHeight())), null);
        } else {
            canvas.drawBitmap(bitmap, new Rect(0,0, bitmap.getWidth(), bitmap.getHeight()),
                    new Rect((canvas.getWidth() - bitmap.getWidth()) / 2,
                            (canvas.getHeight() - bitmap.getHeight()) / 2,
                            (canvas.getWidth() - bitmap.getWidth()) / 2 + bitmap.getWidth(),
                            (canvas.getHeight() - bitmap.getHeight()) / 2 + bitmap.getHeight()), null);
        }

        if (mFpsMeter != null) {
            mFpsMeter.measure();
            mFpsMeter.draw(canvas, 20, 30);
        }
    }

    @Override
    protected void AllocateCache() {
        super.AllocateCache();
        origCachedBitmap = Bitmap.createBitmap(mFrameWidth, mFrameHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onExitStartedState() {
        super.onExitStartedState();
        if (origCachedBitmap != null) {
            origCachedBitmap.recycle();
        }
    }
}
