package com.davidcryer.camerafilters.framework.opencv;

import android.graphics.Canvas;
import android.graphics.Paint;

public class ImageLabel {
    private String text;
    Paint paint;

    public ImageLabel(String text, Paint paint) {
        this.text = text;
        this.paint = paint;
    }

    public void draw(Canvas canvas, float offsetx, float offsety) {
        canvas.drawText(text, offsetx, offsety, paint);
    }

}
