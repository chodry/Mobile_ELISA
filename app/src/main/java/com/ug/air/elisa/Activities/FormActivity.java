package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.LoginActivity.USERNAME;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.FarmerDetails.NAME;
import static com.ug.air.elisa.Fragments.FarmerDetails.START_DATE;
import static com.ug.air.elisa.Fragments.FarmerList.UUID_SPECIAL;
import static com.ug.air.elisa.Fragments.Feeding.DATE_2;
import static com.ug.air.elisa.Fragments.Feeding.DURATION_2;
import static com.ug.air.elisa.Fragments.Feeding.FILENAME_2;
import static com.ug.air.elisa.Fragments.Feeding.INCOMPLETE_2;
import static com.ug.air.elisa.Fragments.Feeding.UNIQUE_2;
import static com.ug.air.elisa.Fragments.Sample.DATE;
import static com.ug.air.elisa.Fragments.Sample.DURATION;
import static com.ug.air.elisa.Fragments.Sample.FILENAME;
import static com.ug.air.elisa.Fragments.Sample.INCOMPLETE;
import static com.ug.air.elisa.Fragments.Sample.UNIQUE;
import static com.ug.air.elisa.Fragments.FarmerList.START_DATE_2;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.Fragments.Camera;
import com.ug.air.elisa.Fragments.FarmerDetails;
import com.ug.air.elisa.Fragments.FarmerList;
import com.ug.air.elisa.Fragments.PatientSignalement;
import com.ug.air.elisa.Fragments.Survey;
import com.ug.air.elisa.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FormActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2, editor3, editor;
    String farm, animal, filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        sharedPreferences2 = getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);
        editor = sharedPreferences.edit();

        animal = sharedPreferences.getString(ANIMAL, "");

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.fragment_container, new FarmerDetails());
////        fragmentTransaction.add(R.id.fragment_container, new Camera());
//        fragmentTransaction.commit();

        Intent intent = getIntent();
        if (intent.hasExtra("farm")){
            farm = intent.getExtras().getString("farm");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (farm.equals("yes")){
                fragmentTransaction.add(R.id.fragment_container, new FarmerDetails());
            }else if (farm.equals("no")){
//                fragmentTransaction.add(R.id.fragment_container, new PatientSignalement());
                fragmentTransaction.add(R.id.fragment_container, new FarmerList());
            }
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        String name = sharedPreferences2.getString(NAME, "");
        String farmer = sharedPreferences2.getString(UUID_SPECIAL, "");

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit);
        dialog.setCancelable(true);

        Button btnYes = dialog.findViewById(R.id.yes);
        Button btnNo = dialog.findViewById(R.id.no);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                String formattedDate = df.format(currentTime);

//                getDuration(currentTime);

                String uniqueID = UUID.randomUUID().toString();

                if (name.isEmpty() && !farmer.isEmpty()){
                    filename = formattedDate + "_" + uniqueID;

                    editor2.putString(DATE, formattedDate);
                    editor2.putString(UNIQUE, uniqueID);
                    editor2.putString(FILENAME, filename);
                    editor2.putString(INCOMPLETE, "incomplete");

                    editor2.apply();
                    doLogic(filename);
                    dialog.dismiss();

                }else if (farmer.isEmpty() && !name.isEmpty()){
                    filename = "farm_" + formattedDate + "_" + uniqueID;

                    editor2.putString(DATE_2, formattedDate);
                    editor2.putString(UNIQUE_2, uniqueID);
                    editor2.putString(FILENAME_2, filename);
                    editor2.putString(INCOMPLETE_2, "incomplete");

                    editor2.apply();
                    doLogic(filename);
                    dialog.dismiss();

                }else if (farmer.isEmpty() && name.isEmpty()){
                    dialog.dismiss();
                    startActivity(new Intent(FormActivity.this, FormMenuActivity.class));
                    finish();
                }

//                if (name.isEmpty() ){
//                    dialog.dismiss();
//                    startActivity(new Intent(FormActivity.this, FormMenuActivity.class));
//                    finish();
//                }
//                else{
//                    Date currentTime = Calendar.getInstance().getTime();
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
//                    String formattedDate = df.format(currentTime);
//
//                    getDuration(currentTime);
//
//                    String uniqueID = UUID.randomUUID().toString();
//
//
//                    if (animal.equals("farm")) {
//                        filename = "farm_" + formattedDate + "_" + uniqueID;
//
//                        editor2.putString(DATE_2, formattedDate);
//                        editor2.putString(UNIQUE_2, uniqueID);
//                        editor2.putString(FILENAME_2, filename);
//                        editor2.putString(INCOMPLETE_2, "incomplete");
////
//                    }else {
//                        filename = formattedDate + "_" + uniqueID;
//
//                        editor2.putString(DATE, formattedDate);
//                        editor2.putString(UNIQUE, uniqueID);
//                        editor2.putString(FILENAME, filename);
//                        editor2.putString(INCOMPLETE, "incomplete");
//
//                    }
//
//                    editor2.apply();
//
//                    doLogic(filename);
//
//                    dialog.dismiss();
//
//                }
            }
        });

        dialog.show();

    }

    private void doLogic(String file) {

        sharedPreferences3 = getSharedPreferences(file, Context.MODE_PRIVATE);
        editor3 = sharedPreferences3.edit();

        Map<String, ?> all = sharedPreferences2.getAll();
        for (Map.Entry<String, ?> x : all.entrySet()) {
            if (x.getValue().getClass().equals(String.class))  editor3.putString(x.getKey(),  (String)x.getValue());
            if (x.getValue().getClass().equals(Boolean.class))  editor3.putBoolean(x.getKey(),  (Boolean) x.getValue());
        }

        editor3.commit();
        editor2.clear();
        editor2.commit();

        startActivity(new Intent(FormActivity.this, FormMenuActivity.class));
    }

    private void getDuration(Date currentTime) {

        if (animal.equals("farm")) {
            String initial_date = sharedPreferences2.getString(START_DATE, "");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
            try {
                Date d1 = format.parse(initial_date);

                long diff = currentTime.getTime() - d1.getTime();//as given

                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                String duration = String.valueOf(minutes);
                editor2.putString(DURATION_2, duration);
                editor2.apply();
                Log.d("Difference in time", "getTimeDifference: " + minutes);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            String initial_date = sharedPreferences2.getString(START_DATE_2, "");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
            try {
                Date d1 = format.parse(initial_date);

                long diff = currentTime.getTime() - d1.getTime();//as given

                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                String duration = String.valueOf(minutes);
                editor2.putString(DURATION, duration);
                editor2.apply();
                Log.d("Difference in time", "getTimeDifference: " + minutes);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
}