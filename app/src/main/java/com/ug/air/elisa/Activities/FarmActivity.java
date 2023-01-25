package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ug.air.elisa.Adapter.FormAdapter;
import com.ug.air.elisa.Models.Form;
import com.ug.air.elisa.R;

import java.util.ArrayList;
import java.util.List;

public class FarmActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    RecyclerView recyclerView;
    FormAdapter formAdapter;
    List<Form> formList;
    String animal;
    ArrayList<String> files;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);

        textView = findViewById(R.id.heading);
        imageView = findViewById(R.id.topBack);
        recyclerView = findViewById(R.id.recyclerview2);

        textView.setText("Farm Details");

        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FarmActivity.this, FormMenuActivity.class));
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);

        animal = sharedPreferences.getString(ANIMAL, "");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
}