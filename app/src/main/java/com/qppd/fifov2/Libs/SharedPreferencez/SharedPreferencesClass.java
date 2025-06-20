package com.qppd.fifov2.Libs.SharedPreferencez;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesClass {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public SharedPreferencesClass(Context context){

        preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();

    }

    public String getString(String key, String defValue){
        return preferences.getString(key, defValue);

    }

    public void putString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }


    public boolean getBoolean(String key, boolean defValue){
        return preferences.getBoolean(key, defValue);
    }


    public void putBoolean(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }


    public int getInt(String key, int defValue){
        return preferences.getInt(key, defValue);
    }


    public void putInt(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }

}
