package com.ug.air.elisa.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.Activities.FormMenuActivity;
import com.ug.air.elisa.R;


public class Survey extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    String season, survey;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int YESS = 0;
    private static final int NOO = 1;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    public static final String SHARED_PREFS_2 = "shared_prefs";
    public static final String SEASON = "season";
    public static final String SURVEY = "survey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_survey, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        radioGroup1 = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.active);
        radioButton2 = view.findViewById(R.id.passive);
        radioGroup2 = view.findViewById(R.id.radioGroup2);
        radioButton3 = view.findViewById(R.id.dry);
        radioButton4 = view.findViewById(R.id.rainy);

        textView.setText("Survey");

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
                        survey = "Active";
                        break;
                    case NO:
                        survey = "Passive";
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
                    case YESS:
                        season = "Dry";
                        break;
                    case NOO:
                        season = "Rainy";
                        break;

                    default:
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (season.isEmpty() || survey.isEmpty()) {
                    Toast.makeText(getActivity(), "Please provide all the data", Toast.LENGTH_SHORT).show();
                }else {
                    saveData();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FormMenuActivity.class));
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(SEASON, season);
        editor2.putString(SURVEY, survey);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new FarmerDetails());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        season = sharedPreferences2.getString(SEASON, "");
        survey = sharedPreferences2.getString(SURVEY, "");
    }

    private void updateViews() {
        if (season.equals("Dry")){
            radioButton3.setChecked(true);
        }else if (season.equals("Rainy")){
            radioButton4.setChecked(true);
        }else {
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
        }

        if (survey.equals("Active")){
            radioButton1.setChecked(true);
        }else if (survey.equals("Passive")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }

    }

}