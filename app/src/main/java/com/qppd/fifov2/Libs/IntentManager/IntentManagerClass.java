package com.qppd.fifov2.Libs.IntentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/*
                    This class is the Activity Manager
                    Changes forms/activities
 */
public class IntentManagerClass {

    private static Intent intent;

    public static void intentsify(Activity fromActivity, Class toClass, int intentFlag){

        intent = new Intent(fromActivity, toClass);
        if(intentFlag != 0){
            intent.addFlags(intentFlag);
        }

        fromActivity.startActivity(intent);
        //fromActivity.finish();
    }

    public static void intentsify(Activity fromActivity, Class toClass){

        intent = new Intent(fromActivity, toClass);
        fromActivity.startActivity(intent);
        //fromActivity.finish();
    }



    public static void intentsify2(Context fromContext, Class toClass, int intentFlag){

        intent = new Intent(fromContext, toClass);
        if(intentFlag != 0){
            intent.addFlags(intentFlag);
        }

        fromContext.startActivity(intent);
        //fromActivity.finish();
    }

    public static void intentCall(final Context context, final Activity activity, final String hotline){
        intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + hotline.trim()));
        if(context != null){
            context.startActivity(intent);
        }
        else if(activity != null){
            activity.startActivity(intent);
        }

    }

//    public static void intentCamera(final Activity activity, final int CAMERA_PIC_REQUEST){
//        intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        activity.startActivityForResult(intent, CAMERA_PIC_REQUEST);
//
//    }

}
