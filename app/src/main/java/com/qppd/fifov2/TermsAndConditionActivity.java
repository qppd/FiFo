package com.qppd.fifov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.qppd.fifov2.Libs.Functionz.UserFunctions;

public class TermsAndConditionActivity extends AppCompatActivity {
    private UserFunctions functions = new UserFunctions(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        functions.noActionBar(getSupportActionBar());

    }
}