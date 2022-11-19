package com.ug.air.elisa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ug.air.elisa.R;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    ProgressBar progressBar;
    EditText etUsername, etPassword;
    String username, password, token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String USERNAME = "username";
    public static final String TOKEN = "token";
    public static final String SHARED_PREFS_1 = "identity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_bar);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);
        editor = sharedPreferences.edit();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (password.isEmpty() || username.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please provide both the username and password", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(LoginActivity.this, PermissionsActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}