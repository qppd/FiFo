package com.qppd.fifov2.Libs.Animationz;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimateViewClass {

    private Animation animation;

    Context context;
    int id;

    public AnimateViewClass(){

    }

    public Animation loadAnimationfromFolder(Context context, int RdotAnimdotName){

        this.context = context;
        this.id = RdotAnimdotName;

        return animation = AnimationUtils.loadAnimation(context, id);
    }

}


// view.startAnimations(animation);