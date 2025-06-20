package com.qppd.fifov2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.qppd.fifov2.Libs.AutoTimez.AutotimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.IntentManager.IntentManagerClass;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.Libs.Validatorz.ValidatorClass;
import com.qppd.fifov2.Tasks.LoginTask;
import com.qppd.fifov2.Tasks.RegisterTask;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private UserFunctions functions = new UserFunctions(this);
    private AutotimeClass autotime = new AutotimeClass(this);
    private SharedPreferencesClass sharedPreferencesClass;

    private EditText edtUsername;
    private EditText edtPassword;

    private TextView txtSignUp;
    private TextView txtForgot;

    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        functions.noActionBar(getSupportActionBar());
        autotime.checkAutotime();
        sharedPreferencesClass = new SharedPreferencesClass(this);

        initComponents();

        if(sharedPreferencesClass.getBoolean("session_status", false)){
            edtUsername.setText(sharedPreferencesClass.getString("session_username", ""));
            edtPassword.setText(sharedPreferencesClass.getString("session_password", ""));
            attemptLogin();
        }else{
            btnSignIn.setEnabled(true);
        }
        //attemptLogin();
        scheduleAlarm();
    }

    private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            //functions.showMessage(sharedPreferencesClass.getInt("setting_notification", 1) + "");
            //long interval = sharedPreferencesClass.getInt("setting_notification", 1) * 60 * 60 * 1000;
            long interval = sharedPreferencesClass.getInt("setting_notification", 1) * 60 * 60 * 1000 ;
            long triggerTime = System.currentTimeMillis() + interval;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, pendingIntent);
        }
    }

    private void initComponents() {

        edtUsername = findViewById(R.id.edtUsername);
        //edtUsername.setText("sajedhm1");
        edtPassword = findViewById(R.id.edtPassword);
        //edtPassword.setText("Jedjed07");


        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        txtForgot = findViewById(R.id.txtForgot);
        txtForgot.setOnClickListener(this);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        btnSignIn.setEnabled(false);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnSignIn:
                //IntentManagerClass.intentsify(this, MenuActivity.class);
                attemptLogin();

                break;
            case R.id.txtSignUp:
                IntentManagerClass.intentsify(this, RegisterActivity.class);
                break;
            case R.id.txtForgot:
                IntentManagerClass.intentsify(this, PasswordForgotActivity.class);
                break;
        }

    }

    private void attemptLogin() {
        // Reset errors.
        edtUsername.setError(null);
        edtPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();


        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Username is empty!");
            focusView = edtUsername;
            cancel = true;
        } else if (!ValidatorClass.validateUsernameOnly(username)) {
            edtUsername.setError("Invalid Username!");
            focusView = edtUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is empty!");
            focusView = edtPassword;
            cancel = true;
        } else if (!ValidatorClass.validatePasswordNoSymbolOnly(password)) {
            edtPassword.setError("Invalid Password!");
            focusView = edtPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {

            LoginTask loginTask = new LoginTask(username, password,this, functions);
            loginTask.execute((Void) null);

        }

    }
}