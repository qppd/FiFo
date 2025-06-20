package com.qppd.fifov2.Libs.Functionz;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;

public class UserFunctions {
	// Context
    private Context context;

    // Constructor
    public UserFunctions(Context context){
        this.context = context;

    }

    public String getIp(){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        }
        return null;
    }

    // set actionbar
    public void setActionbar(ActionBar a, int mode, String title, int resid){
        switch (mode){
            case 0:
                a.hide();

                break;
            case 1:
                //a.setDefaultDisplayHomeAsUpEnabled(true);
                a.setDisplayHomeAsUpEnabled(true);
                //a.setDisplayShowHomeEnabled(true);
                a.setDisplayShowTitleEnabled(true);
                a.setTitle(title);
                break;
            case 2:
                a.setDisplayShowCustomEnabled(true);
                a.setCustomView(resid);
                break;
            case 3:
                //a.setDefaultDisplayHomeAsUpEnabled(true);
                a.setDisplayShowTitleEnabled(true);
                a.setTitle(title);
                break;
        }
    }

    // set actionbar
    public void noActionBar(ActionBar a){
       a.hide();
    }

    // show a toast message
	public void showMessage(String message) {
        Toast msg = Toast.makeText(context, message, Toast.LENGTH_SHORT);

        //msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
        msg.show();
    }

    // get color
    public int COLOR(int colorid) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context,colorid);
        } else {
            return context.getResources().getColor(colorid);
        }
    }

    // dialog for loading
    // pd = new ProgressDialog(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
    public ProgressDialog DIALOG(int theme, int mode){
        ProgressDialog dialog = null;
        switch (mode){
            case 0:
            case 1:
                dialog = new ProgressDialog(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
                break;
        }

        //dialog.show();
        return dialog;
    }


}