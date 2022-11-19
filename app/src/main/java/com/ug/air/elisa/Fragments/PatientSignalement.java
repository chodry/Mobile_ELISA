package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

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

import com.ug.air.elisa.R;


public class PatientSignalement extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etBreed, etWeight, etAge;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    Spinner spinner;
    String time, gender, breed, weight, age, age_2;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    private static final int YES = 0;
    private static final int NO = 1;
    public static final String BREED = "breed";
    public static final String GENDER = "gender";
    public static final String WEIGHT = "weight";
    public static final String AGE = "age";
    public static final String PERIOD_3 = "period_3";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_patient_signalement, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etAge = view.findViewById(R.id.age);
        etBreed = view.findViewById(R.id.breed);
        etWeight = view.findViewById(R.id.weight);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.male);
        radioButton2 = view.findViewById(R.id.female);
        spinner = view.findViewById(R.id.time);

        textView.setText("Patient Signalement");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                breed = etBreed.getText().toString();
                weight = etWeight.getText().toString();
                age = etAge.getText().toString();

                if (breed.isEmpty() || weight.isEmpty() || age.isEmpty() || gender.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide information about other animals", Toast.LENGTH_SHORT).show();
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
                fr.replace(R.id.fragment_container, new FarmAnimals());
                fr.commit();
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        time = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void saveData() {

        editor2.putString(BREED, breed);
        editor2.putString(AGE, age_2);
        editor2.putString(WEIGHT, weight);
        editor2.putString(GENDER, gender);
        editor2.putString(PERIOD_3, age);
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
        weight = sharedPreferences2.getString(WEIGHT, "");
    }

    private void updateViews() {
        etBreed.setText(breed);
        etAge.setText(age);
        etWeight.setText(weight);

        if (gender.equals("Male")){
            radioButton1.setChecked(true);
        }else if (gender.equals("Female")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }
    }
}