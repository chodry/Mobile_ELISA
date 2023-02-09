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


public class Deworming extends Fragment implements AdapterView.OnItemSelectedListener {

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
    public static final String DEWORMING = "deworming";
    public static final String DEWORMING_PERIOD = "deworming_period";
    public static final String MEDICATION_2 = "medication_2";
    public static final String PERIOD_5 = "period_5";
    public static final String TIME_3 = "time_3";
    ArrayAdapter<CharSequence> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deworming, container, false);

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

        textView.setText("De-worming Status");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YES:
                        vaccine = "De-wormed";
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case NO:
                        vaccine = "Not De-wormed";
                        linearLayout.setVisibility(View.GONE);
                        etDate.setText("");
                        etMedication.setText("");
                        break;

                    default:
                        break;
                }
            }
        });

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        updateViews();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medication = etMedication.getText().toString();
                date = etDate.getText().toString();

                if (vaccine.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide the required information", Toast.LENGTH_SHORT).show();
                }else {
                    if (vaccine.equals("De-wormed") && (date.isEmpty() || medication.isEmpty() || time.equals("Select one"))){
                        Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                    }else{
                        if (date.isEmpty()){
                            date_2 = "";
                        }else{
                            date_2 = date + " " + time + " ago";
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
                fr.replace(R.id.fragment_container, new Vaccination());
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
        editor2.putString(DEWORMING, vaccine);
        editor2.putString(DEWORMING_PERIOD, date_2);
        editor2.putString(PERIOD_5, date);
        editor2.putString(MEDICATION_2, medication);
        editor2.putString(TIME_3, time);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Illness());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        vaccine = sharedPreferences2.getString(DEWORMING, "");
        date = sharedPreferences2.getString(PERIOD_5, "");
        medication = sharedPreferences2.getString(MEDICATION_2, "");
        time = sharedPreferences2.getString(TIME_3, "");
    }

    private void updateViews() {
        if (vaccine.equals("De-wormed")){
            radioButton1.setChecked(true);
            linearLayout.setVisibility(View.VISIBLE);
            etDate.setText(date);
            etMedication.setText(medication);
        }else if (vaccine.equals("Not De-wormed")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }

        if (!time.isEmpty()){
            int position = adapter.getPosition(time);
            spinner.setSelection(position);
        }
    }

}