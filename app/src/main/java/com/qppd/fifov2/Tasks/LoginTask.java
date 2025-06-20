package com.qppd.fifov2.Tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.IntentManager.IntentManagerClass;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.MenuActivity;

public class LoginTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private UserFunctions functions;
    private SharedPreferencesClass sharedPreferences;

    private boolean loginStatus = false;

    private String username;
    private String password;

    private DBHandler dbHandler;

    public LoginTask(String username, String password, Context context,
                     UserFunctions functions) {
        this.username = username;
        this.password   = password;
        this.context = context;
        this.functions = functions;
        this.dbHandler = new DBHandler(context);
        this.sharedPreferences = new SharedPreferencesClass(context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        loginStatus = false;
        try {

            if(dbHandler.checkUserExists(username)){
                User user = dbHandler.getUser(username);
                UserGlobal.setUser(user);

                sharedPreferences.putString("session_username", user.getUsername());
                sharedPreferences.putString("session_password", user.getPassword());
                sharedPreferences.putBoolean("session_status", true);

                if(user.getPassword().equals(password)){
                    loginStatus = true;
                }
            }
            else{
                loginStatus = false;
            }

            Thread.sleep(2000);
            return loginStatus;
        } catch (InterruptedException e) {
            return loginStatus = false;
        }

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            //functions.showMessage("LOGIN SUCCESS!");
            IntentManagerClass.intentsify(((Activity) context), MenuActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((Activity) context).finish();
        } else {
            functions.showMessage("Invalid username or password! Please try again!");
        }
    }

    @Override
    protected void onCancelled() {
        functions.showMessage("LOGIN CANCELLED");
    }
}

