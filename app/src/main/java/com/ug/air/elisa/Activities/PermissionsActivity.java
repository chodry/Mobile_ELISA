package com.ug.air.elisa.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.ug.air.elisa.R;
import com.ug.air.elisa.Utils.SimpleMultiplePermissionsListener;

public class PermissionsActivity extends AppCompatActivity {

    Button allowBtn;
    SimpleMultiplePermissionsListener simpleMultiplePermissionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        allowBtn = findViewById(R.id.btn_grant);

        simpleMultiplePermissionsListener = new SimpleMultiplePermissionsListener(this);

        allowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(simpleMultiplePermissionsListener).check();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Intent homeIntent = new Intent(PermissionsActivity.this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK));
            startActivity(homeIntent);
            finish();
        }
        else{
            Toast.makeText(this, "Try pressing the button again to accept some permissions", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPermissionGranted(String permissionName){
        switch (permissionName){
            case Manifest.permission.CAMERA:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                break;
        }
    }

    public void showPermissionsDenied(String permissionName){
        switch (permissionName){
            case Manifest.permission.CAMERA:
                Toast.makeText(this, "Camera permissions Denied", Toast.LENGTH_SHORT).show();
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                Toast.makeText(this, "Storage permissions Denied", Toast.LENGTH_SHORT).show();
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                Toast.makeText(this, "Location permissions Denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showPermissionRational(final PermissionToken token){
        new AlertDialog.Builder(this).setTitle("We need this permission")
                .setMessage("Please allow this permission for do something magic")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.continuePermissionRequest();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.cancelPermissionRequest();
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                }).show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Intent homeIntent = new Intent(PermissionsActivity.this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK));
            startActivity(homeIntent);
            finish();
        }
        else{
            Toast.makeText(this, "Please accept all the required permissions", Toast.LENGTH_SHORT).show();
        }
    }
}