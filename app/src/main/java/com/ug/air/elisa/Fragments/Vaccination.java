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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class Vaccination extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etMedication, etDate;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    Spinner spinner;
    String time, date, medication, vaccine, date_2;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    private static final int YES = 0;
    private static final int NO = 1;
    public static final String VACCINATION = "vaccination";
    public static final String VACCINATION_PERIOD = "vaccination_period";
    public static final String MEDICATION = "medication_1";
    public static final String PERIOD_4 = "period_4";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vacciantion, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etDate = view.findViewById(R.id.date);
        etMedication = view.findViewById(R.id.medication);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.vaccinated);
        radioButton2 = view.findViewById(R.id.not_vaccinated);
        spinner = view.findViewById(R.id.time);
        linearLayout = view.findViewById(R.id.info);

        textView.setText("Vaccination Status");

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
                        vaccine = "Vaccinated";
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case NO:
                        vaccine = "Not Vaccinated";
                        linearLayout.setVisibility(View.GONE);
                        etDate.setText("");
                        etMedication.setText("");
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

                medication = etMedication.getText().toString();
                date = etDate.getText().toString();

                if (vaccine.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }else {
                    if (vaccine.equals("Vaccinated") && (date.isEmpty() || medication.isEmpty())){
                        Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                    }else{
                        if (date.isEmpty()){
                            date_2 = "";
                        }else{
                            date_2 = date + " " + time;
                        }
                        saveData();
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new PatientSignalement());
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

        editor2.putString(VACCINATION, vaccine);
        editor2.putString(VACCINATION_PERIOD, date_2);
        editor2.putString(PERIOD_4, date);
        editor2.putString(MEDICATION, medication);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Deworming());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        vaccine = sharedPreferences2.getString(VACCINATION, "");
        date = sharedPreferences2.getString(PERIOD_4, "");
        medication = sharedPreferences2.getString(MEDICATION, "");
    }

    private void updateViews() {
        if (vaccine.equals("Vaccinated")){
            radioButton1.setChecked(true);
            linearLayout.setVisibility(View.VISIBLE);
            etDate.setText(date);
            etMedication.setText(medication);
        }else if (vaccine.equals("Not Vaccinated")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }
    }

}