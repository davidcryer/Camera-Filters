package com.davidcryer.camerafilters.framework.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.davidcryer.camerafilters.helpers.MetricHelper;

import org.opencv.BuildConfig;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.HashSet;
import java.util.Set;

public class UiLessJavaCameraView extends JavaCameraView {
    private static final float LABEL_TEXT_SIZE_SP = 18.0f;
    private static final float LABEL_X_DP = 24.0f;
    private static final float LABEL_Y_DP = 32.0f;
    private final static String LABEL_ORIG = "Original";
    private final static String LABEL_MOD = "Composited (CPU)";
    private final ImageLabel origLabel;
    private final ImageLabel modLabel;
    private final float labelX;
    private final float labelY;
    private final Set<SurfaceHolder> origHolders = new HashSet<>();
    private final Set<SurfaceHolder> modHolders = new HashSet<>();
    private Bitmap origCachedBitmap;

    public UiLessJavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final Paint labelPaint = new Paint();
        labelPaint.setColor(Color.WHITE);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        labelPaint.setTextSize(MetricHelper.toPix(LABEL_TEXT_SIZE_SP, scale));
        origLabel = new ImageLabel(LABEL_ORIG, labelPaint);
        modLabel = new ImageLabel(LABEL_MOD, labelPaint);
        labelX = MetricHelper.toPix(LABEL_X_DP, scale);
        labelY = MetricHelper.toPix(LABEL_Y_DP, scale);
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
            final Mat unmodifiedClone = unmodified.clone();
            try {
                Utils.matToBitmap(unmodifiedClone, origCachedBitmap);
            } catch(Exception e) {
                Log.e(getClass().getSimpleName(), "Mat type: " + unmodifiedClone);
                Log.e(getClass().getSimpleName(), "Bitmap type: " + origCachedBitmap.getWidth() + "*" + origCachedBitmap.getHeight());
                Log.e(getClass().getSimpleName(), "Utils.matToBitmap() throws an exception: " + e.getMessage());
                bmpValid = false;
            }
            unmodifiedClone.release();
        }

        final Mat modified = mListener != null ? mListener.onCameraFrame(frame) : unmodified;
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
            onto(mCacheBitmap, modLabel, modHolders);
            onto(origCachedBitmap, origLabel, origHolders);
        }
    }

    private void onto(final Bitmap bitmap, final ImageLabel label, final Set<SurfaceHolder> holders) {
        if (bitmap != null) {
            for (final SurfaceHolder holder : holders) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    onto(bitmap, label, canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void onto(final Bitmap bitmap, final ImageLabel label, final Canvas canvas) {
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

        if (label != null) {
            label.draw(canvas, labelX, labelY);
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
