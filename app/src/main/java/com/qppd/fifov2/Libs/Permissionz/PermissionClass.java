package com.qppd.fifov2.Libs.Permissionz;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;


public class PermissionClass {

    Context context;
    String[] permissions;
    Activity activity;

    public PermissionClass(Context context, Activity activity, String... permissions){

        this.context = context;
        this.activity = activity;
        this.permissions = permissions;

    }

    public boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public void requestPermissions(int permit_all){
        ActivityCompat.requestPermissions(activity, permissions, permit_all);
    }


    //
//    int PERMISSION_ALL = 1;
//
//    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.READ_CONTACTS};

//        if (!hasPermissions(this, PERMISSIONS)) {
//        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//    }









}
