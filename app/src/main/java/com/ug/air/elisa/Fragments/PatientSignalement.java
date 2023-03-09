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
import android.widget.LinearLayout;
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
    EditText etTag, etName, etAge;
    LinearLayout linearLayout1, linearLayout2;
    RadioGroup radioGroup, radioGroup2;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    String name, gender, age, tag, animal, start, filename;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int YES2 = 0;
    private static final int NO2 = 1;
    private static final int NOT2 = 2;
    public static final String GENDER = "gender";
    public static final String AGE = "age";
//    public static final String BREED = "breed";
    public static final String ANIMAL_NAME = "animal_name";
    public static final String ANIMAL_TAG = "animal_tag";
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
        etAge = view.findViewById(R.id.age);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.male);
        radioButton2 = view.findViewById(R.id.female);
        radioGroup2 = view.findViewById(R.id.radioGroup2);
        radioButton3 = view.findViewById(R.id.piglet);
        radioButton4 = view.findViewById(R.id.weaner);
        radioButton5 = view.findViewById(R.id.sow);
        linearLayout1 = view.findViewById(R.id.pig_layout);
        linearLayout2 = view.findViewById(R.id.cattle_layout);

        textView.setText("Animal details");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        if (animal.equals("piggery")){
            linearLayout1.setVisibility(View.VISIBLE);
        }else{
            linearLayout2.setVisibility(View.VISIBLE);
        }

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
                        age = "Piglet";
                        break;
                    case NO2:
                        age = "Weaner";
                        break;
                    case NOT2:
                        age = "Sow";
                        break;
                    default:
                        break;
                }
            }
        });

        loadData();
        updateViews();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tag = etTag.getText().toString();
                name = etName.getText().toString();

                if (animal.equals("cattle")){
                    age = etAge.getText().toString();
                }

                if (age.isEmpty() || gender.isEmpty() || tag.isEmpty()){
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

    private void saveData() {

        if (start.isEmpty()){
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            String formattedDate = df.format(currentTime);
            editor2.putString(START_DATE_2, formattedDate);
        }

        editor2.putString(AGE, age);
        editor2.putString(ANIMAL_NAME, name);
        editor2.putString(ANIMAL_TAG, tag);
        editor2.putString(GENDER, gender);
        editor2.putString(MAMMALS, animal);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Breed());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
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

        if (linearLayout1.getVisibility() == View.VISIBLE){
            if (age.equals("Piglet")){
                radioButton3.setChecked(true);
            }else if (age.equals("Weaner")){
                radioButton4.setChecked(true);
            }else if (age.equals("Sow")){
                radioButton5.setChecked(true);
            }else {
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
                radioButton5.setChecked(false);
            }
        }else if (linearLayout2.getVisibility() == View.VISIBLE){
            etAge.setText(age);
        }

        etName.setText(name);
        etTag.setText(tag);

    }
}