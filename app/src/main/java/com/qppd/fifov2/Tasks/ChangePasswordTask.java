package com.qppd.fifov2.Tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;

public class ChangePasswordTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private UserFunctions functions;
    private boolean changePasswordStatus = false;

    private DBHandler dbHandler;
    private User user;

    public ChangePasswordTask(Context context, User user, UserFunctions functions) {
        this.context = context;
        this.user = user;
        this.functions = functions;
        dbHandler = new DBHandler(this.context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        changePasswordStatus = false;
        try {

            if(dbHandler.updatePassword(user)){
                changePasswordStatus = true;
            }else{
                changePasswordStatus = false;
            }

            Thread.sleep(2000);
            return changePasswordStatus;
        } catch (InterruptedException e) {
            return changePasswordStatus = false;
        }

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            functions.showMessage("Password reset successful! You can now sign in!");
            ((Activity) context).finish();
        } else {
            functions.showMessage("Password reset failed! Please try again!");
        }

    }

    @Override
    protected void onCancelled() {
        functions.showMessage("Password reset cancelled!");
    }


}

