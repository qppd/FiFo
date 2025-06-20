package com.qppd.fifov2.Tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.LoginActivity;

public class LogoutTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private UserFunctions functions;

    private boolean logoutStatus = false;

    public LogoutTask(Context context, UserFunctions functions) {
        this.context = context;
        this.functions = functions;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        logoutStatus = false;
        try {



            Thread.sleep(2000);
            return logoutStatus;
        } catch (InterruptedException e) {
            return logoutStatus = false;
        }

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            functions.showMessage("LOGOUT SUCCESS!");
            context.startActivity(new Intent(((Activity) context), LoginActivity.class));
            ((Activity) context).finish();

        } else {
            functions.showMessage("LOGOUT FAILED!");
        }
    }

    @Override
    protected void onCancelled() {
        functions.showMessage("LOGIN CANCELLED");
    }
}

