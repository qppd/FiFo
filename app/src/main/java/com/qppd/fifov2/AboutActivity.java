package com.qppd.fifov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.qppd.fifov2.Libs.Functionz.UserFunctions;

public class AboutActivity extends AppCompatActivity {

    UserFunctions functions = new UserFunctions(this);
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbar_actionbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        functions.noActionBar(getSupportActionBar());
    }
}