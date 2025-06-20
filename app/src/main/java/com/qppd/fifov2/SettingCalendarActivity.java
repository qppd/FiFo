package com.qppd.fifov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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

public class SettingCalendarActivity extends AppCompatActivity {

    private String TAG = "SettingCalendarActivity";

    private DBHandler dbHandler = new DBHandler(this);
    private UserFunctions functions = new UserFunctions(this);
    private AutotimeClass autotime = new AutotimeClass(this);
    private SharedPreferencesClass sharedPreferences;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_calendar);

        sharedPreferences = new SharedPreferencesClass(this);
        functions.noActionBar(getSupportActionBar());
        autotime.checkAutotime();

        initializeComponents();

    }

    private void initializeComponents() {
        toolbar = findViewById(R.id.toolbar_actionbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}