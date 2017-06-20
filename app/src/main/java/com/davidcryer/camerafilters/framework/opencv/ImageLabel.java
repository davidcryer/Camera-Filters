package com.davidcryer.camerafilters.framework.opencv;

import android.graphics.Canvas;
import android.graphics.Paint;

class ImageLabel {
    private final String text;
    private final Paint paint;

    ImageLabel(String text, Paint paint) {
        this.text = text;
        this.paint = paint;
    }

    void draw(Canvas canvas, float offsetx, float offsety) {
        canvas.drawText(text, offsetx, offsety, paint);
    }
}
