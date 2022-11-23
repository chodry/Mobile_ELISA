
package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.PERSON;
import static com.ug.air.elisa.Fragments.GPS.MAMMALS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.R;

import java.io.File;

public class FormMenuActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS_1 = "identity";
    String animal;
    File[] contents;
    int count = 0;
    TextView txtSend;
    ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_menu);

        txtSend = findViewById(R.id.sent);
        imageViewBack = findViewById(R.id.back);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FormMenuActivity.this, HomeActivity.class));
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);
        editor = sharedPreferences.edit();

        animal = sharedPreferences.getString(ANIMAL, "");

        getSavedForms();
    }

    public void new_form(View view) {
        startActivity(new Intent(FormMenuActivity.this, FormActivity.class));
    }

    public void edit_form(View view) {
    }

    public void send_forms(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void getSavedForms() {
        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
        if (src.exists()) {
            File[] contents = src.listFiles();
            if (contents.length != 0) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String name = f.getName().toString();
                        if (!name.equals("shared_prefs.xml") && !name.equals("identity.xml")){
                            String names = name.replace(".xml", "");
                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                            String mammal = sharedPreferences2.getString(MAMMALS, "");
                            if (mammal.equals(animal)){
                                count += 1;
                            }
                        }
                    }
                }
                txtSend.setText("Send Forms ("+ count + ")");
            }
        }
    }
}