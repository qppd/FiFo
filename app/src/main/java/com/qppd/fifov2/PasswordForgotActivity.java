package com.qppd.fifov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qppd.fifov2.Classes.Profile;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.AutoTimez.AutotimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.IntentManager.IntentManagerClass;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.Libs.Validatorz.ValidatorClass;
import com.qppd.fifov2.Tasks.ProfileTask;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "PasswordForgotActivity";

    private DBHandler dbHandler = new DBHandler(this);
    private UserFunctions functions = new UserFunctions(this);
    private AutotimeClass autotime = new AutotimeClass(this);
    private SharedPreferencesClass sharedPreferences;

    private EditText edtUsername;
    private EditText edtPhone;
    private Button btnReset;

    // Define the characters allowed in the OTP
    private static final String ALLOWED_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // Define the length of the OTP
    private static final int OTP_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgot);

        sharedPreferences = new SharedPreferencesClass(this);

        functions.noActionBar(getSupportActionBar());
        autotime.checkAutotime();

        initializeComponents();

//        functions.showMessage(dbHandler.checkPhoneExistsForUser("09634905586", "jejed") + "");
//        functions.showMessage(dbHandler.checkPhoneExistsForUser("09634905586", "sajedhm1") + "");
//        functions.showMessage(dbHandler.checkPhoneExistsForUser("12", "jejed") + "");
    }

    private void initializeComponents() {

        edtUsername = findViewById(R.id.edtUsername);
        //edtUsername.setText("sajedhm1");
        edtPhone = findViewById(R.id.edtPhone);
        //edtPhone.setText("09634905586");
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);


    }

    private void sendMessage(String phone_no, String message) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("apikey", "32f4a56db6455f3c2bdf758ee0108599") // Replace with your actual API key
                .add("number", phone_no)
                .add("message", message)
                .add("sendername", "THESIS")
                .build();

        Request request = new Request.Builder()
                .url("https://semaphore.co/api/v4/messages")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to send message: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Unexpected code " + response);
                } else {
                    Log.d(TAG, "Message sent successfully: " + response.body().string());
                    IntentManagerClass.intentsify(PasswordForgotActivity.this, PasswordResetActivity.class);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReset:
                attemptSend();
                break;
        }
    }

    void attemptSend() {

        // Reset errors.
        edtUsername.setError(null);
        edtPhone.setError(null);

        boolean cancel = false;
        View focusView = null;

        String username = edtUsername.getText().toString();
        String phone = edtPhone.getText().toString();

        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Username is required!");
            focusView = edtUsername;
            cancel = true;
        } else if (!ValidatorClass.validateUsernameOnly(username)) {
            edtUsername.setError("Invalid Username! Username must be 6 characters minimum, contains letter and number!");
            focusView = edtUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone no. is required!");
            focusView = edtPhone;
            cancel = true;
        } else if (!ValidatorClass.validatePhoneOnly(phone)) {
            edtPhone.setError("Invalid! Phone No. is not a philippines valid sim number!");
            focusView = edtPhone;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {
            String otp = generateOTP();
            String message = "You have requested to reset your FIFO's account password. If you did not make this request, please ignore this message.\n" +
                    "\n" +
                    "OTP:" + otp;
            sharedPreferences.putString("username", username);
            sharedPreferences.putString("otp", otp);

            //functions.showMessage(phone + " - " + message );

            sendMessage(phone, message);

        }
    }

    private String generateOTP() {
        // Define the length of the OTP
        int OTP_LENGTH = 6;
        // Define the characters allowed in the OTP
        String digits = "0123456789";
        // Initialize a StringBuilder to store the OTP
        StringBuilder otp = new StringBuilder();
        // Initialize a Random object
        Random random = new Random();

        // Loop to generate OTP
        for (int i = 0; i < OTP_LENGTH; i++) {
            // Generate a random index between 0 and length of digits string
            int index = random.nextInt(digits.length());
            // Append the character at the generated index to the OTP
            otp.append(digits.charAt(index));
        }

        // Convert StringBuilder to String and return OTP
        return otp.toString();
    }
}