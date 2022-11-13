package com.ug.air.elisa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ug.air.elisa.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void cattle(View view) {
        startActivity(new Intent(HomeActivity.this, FormMenuActivity.class));
    }

    public void piggery(View view) {
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