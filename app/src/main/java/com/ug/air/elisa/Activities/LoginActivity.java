package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ug.air.elisa.Apis.ApiClient;
import com.ug.air.elisa.Apis.JsonPlaceHolder;
import com.ug.air.elisa.Models.Token;
import com.ug.air.elisa.Models.User;
import com.ug.air.elisa.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    ProgressBar progressBar;
    EditText etUsername, etPassword;
    String username, password, token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String USERNAME = "username";
    public static final String TOKEN = "token";
    JsonPlaceHolder jsonPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_bar);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        jsonPlaceHolder = ApiClient.getClient().create(JsonPlaceHolder.class);

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
//                    editor.putString(TOKEN, "token");
//                    editor.putString(USERNAME, username);
//                    editor.apply();
//                    startActivity(new Intent(LoginActivity.this, PermissionsActivity.class));
                    sendData();
                }
            }
        });
    }

    private void sendData() {
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
        User user = new User(username, password);
        Call<Token> call = jsonPlaceHolder.login(user);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (!response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    loginBtn.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Connection Issue, Please try again later", Toast.LENGTH_SHORT).show();
                    return;
                }

                String token = response.body().getToken();
                editor.putString(TOKEN, token);
                editor.putString(USERNAME, username);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, PermissionsActivity.class));
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}