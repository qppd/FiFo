package com.qppd.fifov2.Tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;

public class ManageTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private UserFunctions functions;
    private boolean registrationStatus = false;

    private DBHandler dbHandler;
    private User user;

    public ManageTask(Context context, User user, UserFunctions functions) {
        this.context = context;
        this.user = user;
        this.functions = functions;
        dbHandler = new DBHandler(this.context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        registrationStatus = false;
        try {

            if(dbHandler.updateUser(user)){
                registrationStatus = true;
            }else{
                registrationStatus = false;
            }

            Thread.sleep(2000);
            return registrationStatus;
        } catch (InterruptedException e) {
            return registrationStatus = false;
        }

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            functions.showMessage("Account update successful!");
            ((Activity) context).finish();

        } else {
            functions.showMessage("Account update failed! Please try again!");
        }

    }

    @Override
    protected void onCancelled() {
        functions.showMessage("Account update cancelled!");
    }


}

