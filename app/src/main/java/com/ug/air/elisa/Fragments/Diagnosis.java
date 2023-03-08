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

public class Diagnosis extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etTreatment, etDiagnosis, etDiagnosis2;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3;
    String treatment, diagnosis, prognosis, diagnosis2;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int NOT = 2;
    public static final String PROGNOSIS = "prognosis";
    public static final String DIAGNOSIS = "diagnosis";
    public static final String TREAT = "supportive_treatment";
    public static final String OTHER_DIAGNOSIS = "other_diagnosis";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etTreatment = view.findViewById(R.id.treatment);
        etDiagnosis2 = view.findViewById(R.id.other_disease);
        etDiagnosis = view.findViewById(R.id.disease);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.good);
        radioButton2 = view.findViewById(R.id.guarded);
        radioButton3 = view.findViewById(R.id.graver);

        textView.setText("Diagnosis");

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
                        prognosis = "Good";
                        break;
                    case NO:
                        prognosis = "Guarded";
                        break;
                    case NOT:
                        prognosis = "Grave";
                        break;
                    default:
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                treatment = etTreatment.getText().toString();
                diagnosis = etDiagnosis.getText().toString();
                diagnosis2 = etDiagnosis2.getText().toString();

                if (diagnosis.isEmpty() || treatment.isEmpty() || prognosis.isEmpty()){
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
                fr.replace(R.id.fragment_container, new Camera());
                fr.commit();
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(TREAT, treatment);
        editor2.putString(DIAGNOSIS, diagnosis);
        editor2.putString(OTHER_DIAGNOSIS, diagnosis2);
        editor2.putString(PROGNOSIS, prognosis);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Sample());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData(){
        treatment = sharedPreferences2.getString(TREAT, "");
        diagnosis = sharedPreferences2.getString(DIAGNOSIS, "");
        diagnosis2 = sharedPreferences2.getString(OTHER_DIAGNOSIS, "");
        prognosis = sharedPreferences2.getString(PROGNOSIS, "");
    }

    private void updateViews(){
        etDiagnosis.setText(diagnosis);
        etDiagnosis2.setText(diagnosis2);
        etTreatment.setText(treatment);

        switch (prognosis) {
            case "Good":
                radioButton1.setChecked(true);
                break;
            case "Guarded":
                radioButton2.setChecked(true);
                break;
            case "Grave":
                radioButton3.setChecked(true);
                break;
            default:
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                break;
        }
    }
}