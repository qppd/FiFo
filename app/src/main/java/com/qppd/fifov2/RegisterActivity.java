package com.qppd.fifov2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Libs.AutoTimez.AutotimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.IntentManager.IntentManagerClass;
import com.qppd.fifov2.Libs.Validatorz.ValidatorClass;
import com.qppd.fifov2.Tasks.RegisterTask;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private UserFunctions functions = new UserFunctions(this);
    private AutotimeClass autotime = new AutotimeClass(this);

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private Button btnSignUp;

    private TextView txtSignIn;
    private TextView txtTermsAndCondition;
    private TextView txtPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        functions.noActionBar(getSupportActionBar());
        autotime.checkAutotime();
        initComponents();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                attemptSignup();
                break;
            case R.id.txtSignIn:
                finish();
                break;
            case R.id.txtTermsAndCondition:
                //functions.showMessage("terms");
                IntentManagerClass.intentsify(this, TermsAndConditionActivity.class);
                break;
            case R.id.txtPrivacyPolicy:
                //functions.showMessage("privacy");
                IntentManagerClass.intentsify(this, PrivacyPolicyActivity.class);
                break;
        }
    }

    private void initComponents() {
        edtUsername = findViewById(R.id.edtUsername);
        //edtUsername.setText("sajedhm1");
        edtPassword = findViewById(R.id.edtPassword);
        //edtPassword.setText("Jedtala01+");
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        //edtConfirmPassword.setText("Jedtala01+");

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        txtSignIn = findViewById(R.id.txtSignIn);
        txtSignIn.setOnClickListener(this);

        txtTermsAndCondition = findViewById(R.id.txtTermsAndCondition);
        txtTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentManagerClass.intentsify(RegisterActivity.this, TermsAndConditionActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
        });

        txtPrivacyPolicy = findViewById(R.id.txtPrivacyPolicy);
        txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentManagerClass.intentsify(RegisterActivity.this, PrivacyPolicyActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP);

            }
        });

    }

    void attemptSignup() {

        // Reset errors.
        edtUsername.setError(null);
        edtPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
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
            edtPassword.setError("Password is required!");
            focusView = edtPassword;
            cancel = true;
        } else if (!ValidatorClass.validatePasswordNoSymbolOnly(password)) {
            edtPassword.setError("Invalid Password! Password must be 6 characters minimum, contains letter, number!");
            focusView = edtPassword;
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
            User user = new User(username, password);

            RegisterTask registerTask = new RegisterTask(this,  user, functions);
            registerTask.execute((Void) null);

        }
    }

}