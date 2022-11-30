package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class Temperature extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etTemp;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int YES2 = 0;
    private static final int NO2 = 1;
    String lung, heart, temperature;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    public static final String TEMPERATURE = "body_temperature";
    public static final String HEART = "heart_sounds";
    public static final String LUNG = "lung_sounds";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_temperature, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        radioGroup1 = view.findViewById(R.id.radioGroup1);
        radioButton1 = view.findViewById(R.id.normal);
        radioButton2 = view.findViewById(R.id.wheez);
        radioGroup2 = view.findViewById(R.id.radioGroup2);
        radioButton3 = view.findViewById(R.id.silent);
        radioButton4 = view.findViewById(R.id.disorganized);
        etTemp = view.findViewById(R.id.temp);

        textView.setText("Auscultation");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YES:
                        heart = "Yes";
                        break;
                    case NO:
                        heart = "No";
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
                        lung = "Yes";
                        break;
                    case NO2:
                        lung = "No";
                        break;
                    default:
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temperature = etTemp.getText().toString();

                if (temperature.isEmpty() || heart.isEmpty() || lung.isEmpty()){
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
                fr.replace(R.id.fragment_container, new BodyPosture());
                fr.commit();
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(TEMPERATURE, temperature);
        editor2.putString(HEART, heart);
        editor2.putString(LUNG, lung);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Symptoms());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        heart = sharedPreferences2.getString(HEART, "");
        lung = sharedPreferences2.getString(LUNG, "");
        temperature = sharedPreferences2.getString(TEMPERATURE, "");
    }

    private void updateViews() {
        if (heart.equals("Yes")){
            radioButton1.setChecked(true);
        }else if (heart.equals("No")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }

        if (lung.equals("Yes")){
            radioButton3.setChecked(true);
        }else if (lung.equals("No")){
            radioButton4.setChecked(true);
        }else {
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
        }

        etTemp.setText(temperature);
    }
}