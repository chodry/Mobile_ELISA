package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.LoginActivity.TOKEN;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.ug.air.elisa.R;

public class SplashActivity extends AppCompatActivity {

    boolean InternetCheck = true;
    RelativeLayout relativeLayout;
    String token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_splash);

        relativeLayout = findViewById(R.id.relativelayout);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);
        token = sharedPreferences.getString(TOKEN, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSplashTimeout(3000);
    }

    private void startSplashTimeout(int timeout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (token.isEmpty()){
                    boolean InternetResult = checkConnection();
                    if (InternetResult){
                        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    }else{
                        DialogAppear();
                    }
                }else{
                    startActivity(new Intent(SplashActivity.this, PermissionsActivity.class));
                }
            }
        }, timeout);
    }

    private void checkInternetConnection() {
        boolean InternetResult = checkConnection();
        if (InternetResult){
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        }else{
            DialogAppear();
        }

    }

    private void DialogAppear() {
        Snackbar snackbar = Snackbar.make(relativeLayout, " ", Snackbar.LENGTH_INDEFINITE);
        View custom = getLayoutInflater().inflate(R.layout.snack_bar, null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0,0,0,0);
        (custom.findViewById(R.id.connect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        snackbarLayout.addView(custom, 0);
        snackbar.show();
    }

    private boolean checkConnection() {
        if (isOnline()){
            return InternetCheck;
        }else{
            InternetCheck = false;
            return InternetCheck;
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}