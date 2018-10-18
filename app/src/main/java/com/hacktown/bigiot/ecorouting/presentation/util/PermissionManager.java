package com.hacktown.bigiot.ecorouting.presentation.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import com.hacktown.bigiot.ecorouting.R;

public class PermissionManager {

    public static final int MY_PERMISSION_REQUEST_LOCATION = 100;
    public static final int MY_PERMISSION_REQUEST_EXTERNAL_STORAGE = 101;

    public static void checkPermission(@NonNull final Activity activity,
                                       @NonNull final String permission) {

        if (ContextCompat
            .checkSelfPermission(activity.getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                switch (permission){
                    case android.Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        showExplanation(activity, activity.getString(R.string.app_name), activity.getString(R.string.permission_part1)+ permission + activity.getString(R.string.permission_part2), permission, MY_PERMISSION_REQUEST_EXTERNAL_STORAGE);
                        break;
                    case android.Manifest.permission.ACCESS_FINE_LOCATION:
                        showExplanation(activity, activity.getString(R.string.app_name), activity.getString(R.string.permission_part1) + permission + activity.getString(R.string.permission_part2), permission, MY_PERMISSION_REQUEST_LOCATION);
                        break;
                }
            } else {
                switch (permission){
                    case android.Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        requestPermission(activity, permission, MY_PERMISSION_REQUEST_EXTERNAL_STORAGE);
                        break;
                    case android.Manifest.permission.ACCESS_FINE_LOCATION:
                        requestPermission(activity, permission, MY_PERMISSION_REQUEST_LOCATION);
                        break;
                }
            }
        }
    }

    private static void showExplanation(@NonNull final Activity activity,
                                        @NonNull final String title,
                                        @NonNull final String message,
                                        @NonNull final String permission,
                                        final int permissionRequestCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(activity, permission, permissionRequestCode);
                    }
                });
    }

    private static void requestPermission(@NonNull final Activity activity,
                                          @NonNull final String permission,
                                          final int permissionRequestcode) {

        ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionRequestcode);
    }
}



