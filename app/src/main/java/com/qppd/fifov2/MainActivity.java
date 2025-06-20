package com.qppd.fifov2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qppd.fifov2.Classes.Category;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.IntentManager.IntentManagerClass;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGetStarted;

    private UserFunctions functions = new UserFunctions(this);
    private SharedPreferencesClass sharedPreferences;

    private DBHandler dbHandler = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        functions.noActionBar(getSupportActionBar());
        sharedPreferences = new SharedPreferencesClass(this);
        //sharedPreferences.putBoolean("isFirstTime", true);

        IntentManagerClass.intentsify(MainActivity.this, LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();

        if(!sharedPreferences.getBoolean("isFirstTime", true)){
//            IntentManagerClass.intentsify(MainActivity.this, LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            finish();
        }

        setContentView(R.layout.activity_main);
        functions.noActionBar(getSupportActionBar());

        btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGetStarted:

                if(!sharedPreferences.getBoolean("isInitialCategoriesSaved", false)){
                    sharedPreferences.putBoolean("isInitialCategoriesSaved", true);
                    dbHandler.addCategory(new Category("Grocery"));
                    dbHandler.addCategory(new Category("Bills"));
                    dbHandler.addCategory(new Category("Health"));
                    dbHandler.addCategory(new Category("Transaction"));
                }

                IntentManagerClass.intentsify(MainActivity.this, SampleNotificationActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                break;
        }
    }
}