package com.davidcryer.camerafilters.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionHelper {

    public static boolean has(final String permission, final Activity activity) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void request(final String permission, final int requestCode, final Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public static boolean isGranted(final int grantResults) {
        return grantResults == PackageManager.PERMISSION_GRANTED;
    }
}
