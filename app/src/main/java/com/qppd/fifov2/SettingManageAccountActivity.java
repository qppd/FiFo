package com.qppd.fifov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.AutoTimez.AutotimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.Libs.Validatorz.ValidatorClass;
import com.qppd.fifov2.Tasks.ChangePasswordTask;
import com.qppd.fifov2.Tasks.ManageTask;
import com.qppd.fifov2.Tasks.RegisterTask;

public class SettingManageAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SettingManageAccountActivity";

    private DBHandler dbHandler = new DBHandler(this);
    private UserFunctions functions = new UserFunctions(this);
    private AutotimeClass autotime = new AutotimeClass(this);
    private SharedPreferencesClass sharedPreferences;

    private Toolbar toolbar;

    private EditText edtUsername;
    private EditText edtNewPassword;
    private EditText edtConfirmPassword;

    private Button btnSave;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_manage_account);

        sharedPreferences = new SharedPreferencesClass(this);
        functions.noActionBar(getSupportActionBar());
        autotime.checkAutotime();

        initializeComponents();
        loadAccount();

    }

    private void loadAccount(){
        edtUsername.setText(UserGlobal.getUser().getUsername());
    }

    private void initializeComponents() {

        toolbar = findViewById(R.id.toolbar_actionbar);
        toolbar.setOnClickListener(this);

        edtUsername = findViewById(R.id.edtUsername);

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
            case R.id.toolbar_actionbar:
                finish();
                break;
        }
    }


    private void attemptSave() {

        // Reset errors.
        edtUsername.setError(null);
        edtNewPassword.setError(null);
        edtConfirmPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        int id = UserGlobal.getUser().getId();
        String username = edtUsername.getText().toString();
        String password = edtNewPassword.getText().toString();
        String confirm_password = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Username is required!");
            focusView = edtUsername;
            cancel = true;
        } else if (!ValidatorClass.validateUsernameOnly(username)) {
            edtUsername.setError("Invalid Username! Username must be 6 characters minimum, contains letter and number!");
            focusView = edtUsername;
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

            User user = new User(id, username, password);

            ManageTask manageTask = new ManageTask(this,  user, functions);
            manageTask.execute((Void) null);

        }
    }


}