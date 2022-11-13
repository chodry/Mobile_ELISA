package com.ug.air.elisa.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.ug.air.elisa.Fragments.Survey;
import com.ug.air.elisa.R;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new Survey());
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}