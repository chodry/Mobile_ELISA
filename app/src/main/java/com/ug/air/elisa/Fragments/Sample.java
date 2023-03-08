package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.PatientSignalement.ANIMAL_TAG;
import static com.ug.air.elisa.Fragments.PatientSignalement.START_DATE_2;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ug.air.elisa.Activities.FormMenuActivity;
import com.ug.air.elisa.Models.Dewormer;
import com.ug.air.elisa.Models.Name;
import com.ug.air.elisa.R;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class Sample extends Fragment {

    View view;
    Button backBtn, nextBtn, addBtn;
    TextView textView;
    String sample_name, animal;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2, editor3;
    public static final String SAMPLE = "sample";

    public static final String UNIQUE = "unique_id";
    public static final String FILENAME = "filename";
    public static final String DATE = "created_on";
    public static final String DURATION = "duration";
    public static final String INCOMPLETE = "incomplete";

    List<String> sampleList = new ArrayList<>();
    ArrayList<Name> nameArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sample, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        addBtn = view.findViewById(R.id.add);
        textView = view.findViewById(R.id.heading);
        linearLayout = view.findViewById(R.id.layout_list);

        textView.setText("Clinical Samples");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        sampleList.add("Select sample type...");
        sampleList.add("Whole Blood");
        sampleList.add("Serum");
        sampleList.add("Nasal Swab");

        if (animal.equals("cattle")){
            sampleList.add("Oral Swab");
            sampleList.add("Probang");
            sampleList.add("Hoof Sample");
            sampleList.add("Fecal Sample");
        }

        loadData();
        updateViews();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkIfValidAndRead();
                saveForm();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Diagnosis());
                fr.commit();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        return view;
    }

    private void addView() {
        View sampleView = getLayoutInflater().inflate(R.layout.sample,null,false);

        TextView textView1 = sampleView.findViewById(R.id.sample);
        TextView textView2 = sampleView.findViewById(R.id.sample_name);
        AppCompatSpinner spinner = sampleView.findViewById(R.id.spinner);
        ImageView imageClose = sampleView.findViewById(R.id.close);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, sampleList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = parent.getItemAtPosition(position).toString();
                if (!time.equals("Select sample type...")){
                    textView1.setText(time);
                    generateSampleName(textView2, time);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("");
            }
        });

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(sampleView);
            }
        });

        linearLayout.addView(sampleView);
    }

    private void removeView(View sampleView) {
        linearLayout.removeView(sampleView);
    }

    private boolean checkIfValidAndRead() {
        nameArrayList.clear();
        boolean result = true;

        for (int i=0; i<linearLayout.getChildCount(); i++){
            View sampleView = linearLayout.getChildAt(i);
            TextView textView1 = sampleView.findViewById(R.id.sample);
            TextView textView2 = sampleView.findViewById(R.id.sample_name);

            Name name = new Name();

            if(!textView1.getText().toString().isEmpty()){
                name.setSample(textView1.getText().toString());
            }else {
                result = false;
                break;
            }

            if (!textView2.getText().toString().isEmpty()){
                name.setSample_name(textView2.getText().toString());
            }else {
                result = false;
                break;
            }

            nameArrayList.add(name);
        }

        if (nameArrayList.size() == 0) {
            result = false;
        }
        else {
            Gson gson = new Gson();

            String json = gson.toJson(nameArrayList);
            editor2.putString(SAMPLE, json);
            editor2.apply();
        }

        return result;
    }


    private void saveData() {
//        saveForm();
    }

    private void loadData() {

        Gson gson = new Gson();
        String json = sharedPreferences2.getString(SAMPLE, null);
        Type type = new TypeToken<ArrayList<Name>>() {}.getType();
        nameArrayList = gson.fromJson(json, type);
        if (nameArrayList == null) {
            nameArrayList = new ArrayList<>();
        }else {
            for (Name cri: nameArrayList){
                View sampleView = getLayoutInflater().inflate(R.layout.sample,null,false);

                TextView textView1 = sampleView.findViewById(R.id.sample);
                TextView textView2 = sampleView.findViewById(R.id.sample_name);
                AppCompatSpinner spinner = sampleView.findViewById(R.id.spinner);
                ImageView imageClose = sampleView.findViewById(R.id.close);

                textView1.setText(cri.getSample());
                textView2.setText(cri.getSample_name());

                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, sampleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        String time = parent.getItemAtPosition(position).toString();
//                        if (!time.equals("Select sample type...")){
//                            textView1.setText(time);
//                            generateSampleName(textView2, time);
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                        textView.setText("");
//                    }
//                });
                spinner.setSelection(arrayAdapter.getPosition(cri.getSample()));

                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeView(sampleView);
                    }
                });

                linearLayout.addView(sampleView);
            }
        }

    }

    private void updateViews() {

    }

    public void saveForm(){

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        String formattedDate = df.format(currentTime);

        getDuration(currentTime);

        String uniqueID = UUID.randomUUID().toString();
        String filename = formattedDate + "_" + uniqueID;

//        editor2.putString(MAMMALS, animal);
        editor2.putString(UNIQUE, uniqueID);
        editor2.putString(DATE, formattedDate);
        editor2.putString(FILENAME, filename);
        editor2.putString(INCOMPLETE, "complete");
        editor2.apply();

        sharedPreferences3 = requireActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        editor3 = sharedPreferences3.edit();

        Map<String, ?> all = sharedPreferences2.getAll();
        for (Map.Entry<String, ?> x : all.entrySet()) {
            if (x.getValue().getClass().equals(String.class))  editor3.putString(x.getKey(),  (String)x.getValue());
            if (x.getValue().getClass().equals(Boolean.class))  editor3.putBoolean(x.getKey(),  (Boolean) x.getValue());
            if (x.getValue().getClass().equals(Integer.class))  editor3.putInt(x.getKey(),  (Integer) x.getValue());
        }

        editor3.commit();
        editor2.clear();
        editor2.commit();
        startActivity(new Intent(getActivity(), FormMenuActivity.class));
    }

    private void getDuration(Date currentTime) {
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

    private void generateSampleName(TextView textView2, String time) {
        String tag = sharedPreferences2.getString(ANIMAL_TAG, "");
        String uuid = UUID.randomUUID().toString().substring(0,5);
        String short_code = "";
        String ani = "";
        if (time.equals("Whole Blood")){
            short_code = "wb";
        }
        else if (time.equals("Serum")){
            short_code = "se";
        }
        else if (time.equals("Nasal Swab")){
            short_code = "ns";
        }
        else if (time.equals("Oral Swab")){
            short_code = "os";
        }
        else if (time.equals("Probang")){
            short_code = "pb";
        }
        else if (time.equals("Hoof Sample")){
            short_code = "hs";
        }
        else if (time.equals("Fecal Sample")){
            short_code = "fs";
        }

        if (animal.equals("cattle")){
            ani = "ca";
        }else{
            ani = "pi";
        }

        String sti_code = short_code + '-' + ani + "-TAG_" + tag + '-' + uuid;
        sti_code = sti_code.toUpperCase();
        textView2.setText(sti_code);
    }
}