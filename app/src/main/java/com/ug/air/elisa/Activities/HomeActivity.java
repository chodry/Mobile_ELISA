package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.WelcomeActivity.PERSON;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ug.air.elisa.R;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS_1 = "identity";
    public static final String ANIMAL = "animal";
    String person, animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);
        editor = sharedPreferences.edit();

        person = sharedPreferences.getString(PERSON, "");
    }

    public void cattle(View view) {
        editor.putString(ANIMAL, "cattle");
        editor.apply();
        startActivity(new Intent(HomeActivity.this, FormMenuActivity.class));
    }

    public void piggery(View view) {
        editor.putString(ANIMAL, "piggery");
        editor.apply();
        startActivity(new Intent(HomeActivity.this, FormMenuActivity.class));
    }

    public void laboratory(View view) {
    }

    public void contact(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}