package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Deworming extends Fragment {

    View view;
    Button backBtn, nextBtn, datePickerBtn;
    TextView textView, txtDate;
    EditText etMedication;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    String time, date, medication, vaccine, date_2, animal;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2, editor;
    private static final int YES = 0;
    private static final int NO = 1;
    public static final String DEWORMING = "deworming";
    public static final String DATE_D = "date_d";
    public static final String DEWORMING_DATE = "deworming_date";
    public static final String DEWORMER = "dewormer";
    DatePickerDialog datePickerDialog;
    int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deworming, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        datePickerBtn = view.findViewById(R.id.datepicker);
        textView = view.findViewById(R.id.heading);
        txtDate = view.findViewById(R.id.date);
        etMedication = view.findViewById(R.id.medication);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.vaccinated);
        radioButton2 = view.findViewById(R.id.not_vaccinated);
        linearLayout = view.findViewById(R.id.info);

        textView.setText("Deworming Status");

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

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
                        vaccine = "Dewormed";
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case NO:
                        vaccine = "Not Dewormed";
                        linearLayout.setVisibility(View.GONE);
                        txtDate.setText("");
                        etMedication.setText("");
                        break;

                    default:
                        break;
                }
            }
        });

        updateViews();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medication = etMedication.getText().toString();
                date = txtDate.getText().toString();

                if (vaccine.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide the required information", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (vaccine.equals("Dewormed") && (date.isEmpty() || medication.isEmpty())){
                        Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                    }else{
                        convertDate(date);
                        saveData();
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                if (animal.equals("piggery")){
                    fr.replace(R.id.fragment_container, new Breed());
                }else{
                    fr.replace(R.id.fragment_container, new Vaccination());
                }

                fr.commit();
            }
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR); // current year
                month = c.get(Calendar.MONTH); // current month
                day = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        return view;
    }

    private void saveData() {
        editor2.putString(DEWORMING, vaccine);
        editor2.putString(DEWORMING_DATE, date_2);
        editor2.putString(DATE_D, date);
        editor2.putString(DEWORMER, medication);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Illness());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        vaccine = sharedPreferences2.getString(DEWORMING, "");
        date = sharedPreferences2.getString(DATE_D, "");
        medication = sharedPreferences2.getString(DEWORMER, "");
    }

    private void updateViews() {
        if (vaccine.equals("Dewormed")){
            radioButton1.setChecked(true);
            linearLayout.setVisibility(View.VISIBLE);
            txtDate.setText(date);
            etMedication.setText(medication);
        }else if (vaccine.equals("Not Dewormed")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }

    }

    private void convertDate(String calDate){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(calDate);
            SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            date_2 = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}