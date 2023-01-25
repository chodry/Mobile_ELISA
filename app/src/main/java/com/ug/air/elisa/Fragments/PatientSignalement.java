package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class PatientSignalement extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etBreed, etWeight, etAge;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    Spinner spinner, spinner2;
    String time, gender, breed, age, age_2, animal, start, filename;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2;
    private static final int YES = 0;
    private static final int NO = 1;
    public static final String BREED = "breed";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String PERIOD_3 = "period_3";
    public static final String TIME_1 = "time_1";
    ArrayAdapter<CharSequence> adapter, adapter2;
    public static final String START_DATE_2 = "start_date";
    public static final String MAMMALS = "mammal";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_patient_signalement, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etAge = view.findViewById(R.id.age);
        spinner2 = view.findViewById(R.id.breed);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.male);
        radioButton2 = view.findViewById(R.id.female);
        spinner = view.findViewById(R.id.time);

        textView.setText("Patient Signalement");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YES:
                        gender = "Male";
                        break;
                    case NO:
                        gender = "Female";
                        break;

                    default:
                        break;
                }
            }
        });

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new TimeSpinnerClass());

        adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.breed, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new BreedSpinnerClass());

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("filename")) {
            filename = intent.getExtras().getString("filename");
            sharedPreferences3 = requireActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
            sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, Context.MODE_PRIVATE);

            editor2 = sharedPreferences2.edit();
            Map<String, ?> all = sharedPreferences3.getAll();
            for (Map.Entry<String, ?> x : all.entrySet()) {
                if (x.getValue().getClass().equals(String.class))  editor2.putString(x.getKey(),  (String)x.getValue());
                if (x.getValue().getClass().equals(Boolean.class))  editor2.putBoolean(x.getKey(),  (Boolean) x.getValue());
            }
            editor2.commit();
            editor2.apply();

            filename = filename + ".xml";
            File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + filename);
            if (src.exists()){
                src.delete();
            }

        }else {
            sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
            editor2 = sharedPreferences2.edit();
        }

        loadData();
        updateViews();
//        spinner2.setSelection(2);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                breed = etBreed.getText().toString();
                age = etAge.getText().toString();

                if (breed.equals("Select one") || breed.isEmpty() || age.isEmpty() || gender.isEmpty() || time.equals("Select one")){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }else {
                    age_2 = age + " " + time;
                    saveData();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Cattle());
                fr.commit();
            }
        });

        return view;
    }

    public class TimeSpinnerClass implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            time = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class BreedSpinnerClass implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            breed = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void saveData() {

        if (start.isEmpty()){
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            String formattedDate = df.format(currentTime);
            editor2.putString(START_DATE_2, formattedDate);
        }

        editor2.putString(BREED, breed);
        editor2.putString(AGE, age_2);
        editor2.putString(GENDER, gender);
        editor2.putString(PERIOD_3, age);
        editor2.putString(TIME_1, time);
        editor2.putString(MAMMALS, animal);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Vaccination());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        breed = sharedPreferences2.getString(BREED, "");
        age = sharedPreferences2.getString(PERIOD_3, "");
        gender = sharedPreferences2.getString(GENDER, "");
        time = sharedPreferences2.getString(TIME_1, "");
        start = sharedPreferences2.getString(START_DATE_2, "");
    }

    private void updateViews() {
//        etBreed.setText(breed);
        etAge.setText(age);

        if (gender.equals("Male")){
            radioButton1.setChecked(true);
        }else if (gender.equals("Female")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }

        if (!breed.isEmpty()){
            int position = adapter2.getPosition(breed);
            spinner2.setSelection(position);
        }

        if (!time.isEmpty()){
            int position = adapter.getPosition(time);
            spinner.setSelection(position);
        }

    }
}