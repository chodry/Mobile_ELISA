
package com.ug.air.elisa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ug.air.elisa.R;

public class FormMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_menu);
    }

    public void new_form(View view) {
        startActivity(new Intent(FormMenuActivity.this, FormActivity.class));
    }

    public void edit_form(View view) {
    }

    public void send_forms(View view) {
    }
}