package com.ug.air.elisa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ug.air.elisa.R;

public class WelcomeActivity extends AppCompatActivity {

    Button vetBtn, labBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PERSON = "person";
    public static final String SHARED_PREFS_1 = "identity";
    String person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_welcome);

        vetBtn = findViewById(R.id.vet);
        labBtn = findViewById(R.id.lab);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);
        editor = sharedPreferences.edit();

        vetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(PERSON, "vet");
                editor.apply();
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });

        labBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(PERSON, "lab");
                editor.apply();
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}