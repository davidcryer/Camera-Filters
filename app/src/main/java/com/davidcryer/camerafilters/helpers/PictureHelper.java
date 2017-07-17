package com.davidcryer.camerafilters.helpers;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PictureHelper {

    public static String defaultName() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ENGLISH);
        final String currentDateAndTime = sdf.format(new Date());
        return Environment.getExternalStorageDirectory().getPath() +
                "/filtered_image_" + currentDateAndTime + ".jpg";
    }

    public static void save(final byte[] data, final String fileName) {
        try {
            final FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }

    public static void saveAndRecycle(final Bitmap bitmap, final String fileName) {
        try {
            final FileOutputStream fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bitmap.recycle();
            fos.close();
        } catch (IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }
}
