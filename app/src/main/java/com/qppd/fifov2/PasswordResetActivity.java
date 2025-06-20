package com.qppd.fifov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Libs.AutoTimez.AutotimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.Libs.Validatorz.ValidatorClass;
import com.qppd.fifov2.Tasks.ChangePasswordTask;
import com.qppd.fifov2.Tasks.RegisterTask;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "PasswordResetActivity";

    private DBHandler dbHandler = new DBHandler(this);
    private UserFunctions functions = new UserFunctions(this);
    private AutotimeClass autotime = new AutotimeClass(this);
    private SharedPreferencesClass sharedPreferences;

    private EditText edtCode;
    private EditText edtNewPassword;
    private EditText edtConfirmPassword;

    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        sharedPreferences = new SharedPreferencesClass(this);

        functions.noActionBar(getSupportActionBar());
        autotime.checkAutotime();

        initializeComponents();

    }

    private String getOTPCode() {
        return sharedPreferences.getString("otp", "");
    }

    private String getUsername(){
        return sharedPreferences.getString("username", "");
    }

    private void initializeComponents() {

        edtCode = findViewById(R.id.edtCode);

        edtNewPassword = findViewById(R.id.edtNewPassword);
        //edtNewPassword.setText("Jedjed07");
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        //edtConfirmPassword.setText("Jedjed07");

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                attemptSave();
                break;
        }
    }


    private void attemptSave() {

        // Reset errors.
        edtCode.setError(null);
        edtNewPassword.setError(null);
        edtConfirmPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        String code = edtCode.getText().toString();
        String password = edtNewPassword.getText().toString();
        String confirm_password = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(code)) {
            edtCode.setError("OTP Code is required!");
            focusView = edtCode;
            cancel = true;
        } else if (code.length() != 6) {
            edtCode.setError("Invalid One Time Password! OTP must be 6 numerical character!");
            focusView = edtCode;
            cancel = true;
        } else if(!code.equals(getOTPCode())){
            edtCode.setError("Invalid One Time Password! Try again!");
            focusView = edtCode;
            cancel = true;
        }


        if (TextUtils.isEmpty(password)) {
            edtNewPassword.setError("Password is required!");
            focusView = edtNewPassword;
            cancel = true;
        } else if (!ValidatorClass.validatePasswordNoSymbolOnly(password)) {
            edtNewPassword.setError("Invalid Password! Password must be 6 characters minimum, contains letter, number!");
            focusView = edtNewPassword;
            cancel = true;
        } else if (!password.equals(confirm_password)) {
            edtConfirmPassword.setError("Password do not match!");
            focusView = edtConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {

            User user = new User(getUsername(), password);

            ChangePasswordTask changePasswordTask = new ChangePasswordTask(this,  user, functions);
            changePasswordTask.execute((Void) null);

        }
    }
}