package com.qppd.fifov2.Tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.qppd.fifov2.Classes.Profile;
import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;

public class ProfileTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private UserFunctions functions;
    private SharedPreferencesClass sharedPreferences;
    private boolean saveStatus = false;

    private DBHandler dbHandler;
    private Profile profile;

    public ProfileTask(Context context, Profile profile, UserFunctions functions) {
        this.context = context;
        this.profile = profile;
        this.functions = functions;
        this.sharedPreferences = new SharedPreferencesClass(context);
        dbHandler = new DBHandler(this.context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        saveStatus = false;
        try {

            if(dbHandler.updateProfile(profile)){
                saveStatus = true;
                sharedPreferences.putBoolean("isFirstTime", false);
            }else{
                saveStatus = false;
            }

            Thread.sleep(2000);
            return saveStatus;
        } catch (InterruptedException e) {
            return saveStatus = false;
        }

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            functions.showMessage("Profile saved successfully!");
            //((Activity) context).finish();
        } else {
            functions.showMessage("Saving profile failed! Please try again!");
        }

    }

    @Override
    protected void onCancelled() {
        functions.showMessage("Saving profile cancelled!");
    }


}

