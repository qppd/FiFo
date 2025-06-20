package com.qppd.fifov2.Libs.AutoTimez;

import android.content.Context;
import android.content.Intent;

public class AutotimeClass {

    private Context context;

    public AutotimeClass(){
    }

    public AutotimeClass(Context context){
        this.context = context;
    }


    String timeSettings;

    public void checkAutotime(){

        timeSettings = android.provider.Settings.System.getString(
                context.getContentResolver(),
                android.provider.Settings.Global.AUTO_TIME);

        if (timeSettings.contentEquals("0")) {
            this.context.startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
        }

    }



}
