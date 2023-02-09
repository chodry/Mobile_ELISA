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
    EditText etTag, etName;
    RadioGroup radioGroup, radioGroup2;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    Spinner spinner, spinner2;
    String name, gender, breed, age, tag, animal, start, filename;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int YES2 = 0;
    private static final int NO2 = 1;
    private static final int NOT2 = 2;
    public static final String BREED = "breed";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String ANIMAL_NAME = "animal_name";
    public static final String ANIMAL_TAG = "animal_tag";
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
        etTag = view.findViewById(R.id.tag);
        etName = view.findViewById(R.id.name);
        spinner2 = view.findViewById(R.id.breed);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.male);
        radioButton2 = view.findViewById(R.id.female);
        radioGroup2 = view.findViewById(R.id.radioGroup2);
        radioButton3 = view.findViewById(R.id.adult);
        radioButton4 = view.findViewById(R.id.heifer);
        radioButton5 = view.findViewById(R.id.calf);
//        spinner = view.findViewById(R.id.time);

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

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YES2:
                        age = "Adult";
                        break;
                    case NO2:
                        age = "Heifer";
                        break;
                    case NOT2:
                        age = "Calf";
                        break;
                    default:
                        break;
                }
            }
        });

//        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.time, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new TimeSpinnerClass());

        adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.breed, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new BreedSpinnerClass());

        loadData();
        updateViews();
//        spinner2.setSelection(2);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tag = etTag.getText().toString();
                name = etName.getText().toString();

                if (breed.equals("Select one") || breed.isEmpty() || age.isEmpty() || gender.isEmpty() || tag.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }else {
                    saveData();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new FarmerList());
                fr.commit();
            }
        });

        return view;
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
        editor2.putString(AGE, age);
        editor2.putString(ANIMAL_NAME, name);
        editor2.putString(ANIMAL_TAG, tag);
        editor2.putString(GENDER, gender);
        editor2.putString(MAMMALS, animal);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Vaccination());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        breed = sharedPreferences2.getString(BREED, "");
        age = sharedPreferences2.getString(AGE, "");
        tag = sharedPreferences2.getString(ANIMAL_TAG, "");
        name = sharedPreferences2.getString(ANIMAL_NAME, "");
        gender = sharedPreferences2.getString(GENDER, "");
        start = sharedPreferences2.getString(START_DATE_2, "");
    }

    private void updateViews() {

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

        if (age.equals("Adult")){
            radioButton3.setChecked(true);
        }else if (age.equals("Heifer")){
            radioButton4.setChecked(true);
        }else if (age.equals("Calf")){
            radioButton5.setChecked(true);
        }else {
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
        }

        etName.setText(name);
        etTag.setText(tag);

    }
}