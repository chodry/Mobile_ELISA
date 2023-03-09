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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Survey extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView, txtMonth;
    String season, survey, disease, month;
    RadioGroup radioGroup1, radioGroup2, radioGroup3;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6, radioButton7, radioButtonX;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int NOT = 2;

    private static final int YESS = 0;
    private static final int NOO = 1;

    private static final int YESX = 0;
    private static final int NOX = 1;
    private static final int NOXX = 2;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    public static final String SHARED_PREFS_2 = "shared_prefs";
    public static final String SEASON = "season";
    public static final String MONTH = "season_month";
    public static final String SURVEY = "survey";

    public static final String DISEASE = "disease";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_survey, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        txtMonth = view.findViewById(R.id.month);
        radioGroup1 = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.active);
        radioButton2 = view.findViewById(R.id.passive);
        radioButtonX = view.findViewById(R.id.cross);

        radioGroup2 = view.findViewById(R.id.radioGroup2);
        radioButton3 = view.findViewById(R.id.dry);
        radioButton4 = view.findViewById(R.id.rainy);

        radioGroup3 = view.findViewById(R.id.radioGroup3);
        radioButton5 = view.findViewById(R.id.swine);
        radioButton6 = view.findViewById(R.id.fnd);
        radioButton7 = view.findViewById(R.id.both);

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
                        survey = "Active/ Outbreak response";
                        break;
                    case NO:
                        survey = "Passive";
                        break;
                    case NOT:
                        survey = "Cross-sectional";
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YESX:
                        disease = "African Swine Fever";
                        break;
                    case NOX:
                        disease = "Foot-and-Mouth Disease";
                        break;
                    case NOXX:
                        disease = "Both";
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

                month = getMonth();
                txtMonth.setText("Month: " + month);
                txtMonth.setVisibility(View.VISIBLE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (season.isEmpty() || survey.isEmpty() || disease.isEmpty() || month.isEmpty()) {
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
                fr.replace(R.id.fragment_container, new FarmerDetails());
                fr.commit();
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(SEASON, season);
        editor2.putString(SURVEY, survey);
        editor2.putString(DISEASE, disease);
        editor2.putString(MONTH, month);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new FarmHistory());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        season = sharedPreferences2.getString(SEASON, "");
        survey = sharedPreferences2.getString(SURVEY, "");
        disease = sharedPreferences2.getString(DISEASE, "");
        month = sharedPreferences2.getString(MONTH, "");
    }

    private void updateViews() {
        if (season.equals("Dry")){
            radioButton3.setChecked(true);
            txtMonth.setText("Month: " + month);
            txtMonth.setVisibility(View.VISIBLE);
        }else if (season.equals("Rainy")){
            radioButton4.setChecked(true);
            txtMonth.setText("Month: " + month);
            txtMonth.setVisibility(View.VISIBLE);
        }else {
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
        }

        if (survey.equals("Active/ Outbreak response")){
            radioButton1.setChecked(true);
        }else if (survey.equals("Passive")){
            radioButton2.setChecked(true);
        }else if (survey.equals("Cross-sectional")){
            radioButtonX.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButtonX.setChecked(false);
        }

        if (disease.equals("African Swine Fever")){
            radioButton5.setChecked(true);
        }else if (disease.equals("Foot-and-Mouth Disease")){
            radioButton6.setChecked(true);
        }else if (disease.equals("Both")){
            radioButton7.setChecked(true);
        }else {
            radioButton5.setChecked(false);
            radioButton6.setChecked(false);
            radioButton7.setChecked(false);
        }

    }

    public String getMonth(){
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date date = new Date();
        return dateFormat.format(date);
    }

}