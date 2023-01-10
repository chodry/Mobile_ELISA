package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.FarmerDetails.DISTRICT;
import static com.ug.air.elisa.Fragments.FarmerDetails.MAMMALS;
import static com.ug.air.elisa.Fragments.FarmerDetails.NAME;
import static com.ug.air.elisa.Fragments.FarmerDetails.PARISH;
import static com.ug.air.elisa.Fragments.FarmerDetails.SUB_COUNTY;
import static com.ug.air.elisa.Fragments.FarmerDetails.VILLAGE;
import static com.ug.air.elisa.Fragments.GPS.DATE;
import static com.ug.air.elisa.Fragments.GPS.FILENAME;
import static com.ug.air.elisa.Fragments.GPS.INCOMPLETE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ug.air.elisa.Adapter.FormAdapter;
import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.Models.Form;
import com.ug.air.elisa.R;

import java.io.File;
import java.net.FileNameMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ListActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_list);

        textView = findViewById(R.id.heading);
        imageView = findViewById(R.id.topBack);
        recyclerView = findViewById(R.id.recyclerview);

        textView.setText("Click to edit form");

        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, FormMenuActivity.class));
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);

        animal = sharedPreferences.getString(ANIMAL, "");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        formList = new ArrayList<>();
        files = new ArrayList<String>();
        accessSharedFile();

        formAdapter = new FormAdapter(formList, this);
        recyclerView.setAdapter(formAdapter);

        formAdapter.setOnItemClickListener(new FormAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Form form = formList.get(position);
                String file = form.getFilename();
                Intent intent = new Intent(ListActivity.this, FormActivity.class);
                intent.putExtra("filename", file);
                startActivity(intent);
            }
        });
    }

    private void accessSharedFile() {
        files.clear();
        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
        if (src.exists()) {
            File[] contents = src.listFiles();
            if (contents.length != 0) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String name = f.getName().toString();
                        files.add(name);
                    }
                }
                Collections.reverse(files);
                for(String name : files){
                    if (!name.equals("shared_prefs.xml") && !name.equals("identity.xml")){

                        Log.d("ELISA App", "accessSharedFile: "+ name);

                        String names = name.replace(".xml", "");
                        SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                        String mammal = sharedPreferences2.getString(MAMMALS, "");

                        if (mammal.equals(animal)){
                            String incomplete = sharedPreferences2.getString(INCOMPLETE, "");
                            String filename = sharedPreferences2.getString(FILENAME, "");
                            String farmer = sharedPreferences2.getString(NAME, "");
                            String village = sharedPreferences2.getString(VILLAGE, "");
                            String subCounty = sharedPreferences2.getString(SUB_COUNTY, "");
                            String parish = sharedPreferences2.getString(PARISH, "");
                            String district = sharedPreferences2.getString(DISTRICT, "");
                            String dat = sharedPreferences2.getString(DATE, "");
                            String location = district + "-" + subCounty + "-" + parish + "-" + village;
                            Form form = new Form(farmer, location, dat, mammal, filename);
                            formList.add(form);
                        }

                    }
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListActivity.this, FormMenuActivity.class));
        finish();
    }
}