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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ug.air.elisa.Models.Dewormer;
import com.ug.air.elisa.R;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Vaccination extends Fragment {

    String time, date, medication, vaccine, date_2, animal;
    LinearLayout linearLayout, linearLayout2;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2, editor;
    private static final int YES = 0;
    private static final int NO = 1;
    public static final String VACCINATION = "vaccination";
    public static final String VACCINE = "vaccine_info";
    View view;
    Button backBtn, nextBtn, addBtn;
    TextView textView, txtDate;
    EditText etMedication;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    DatePickerDialog datePickerDialog;
    int year, month, day;
    ArrayList<Dewormer> dewomerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vacciantion, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.vaccinated);
        radioButton2 = view.findViewById(R.id.not_vaccinated);
        linearLayout = view.findViewById(R.id.layout_list);
        linearLayout2 = view.findViewById(R.id.info);
        addBtn = view.findViewById(R.id.add);

        textView.setText("Vaccination Status");

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
                        vaccine = "Vaccinated";
                        linearLayout2.setVisibility(View.VISIBLE);
                        break;
                    case NO:
                        vaccine = "Not Vaccinated";
                        linearLayout2.setVisibility(View.GONE);
                        editor2.putString(VACCINE, "[]");
                        editor2.apply();
                        break;

                    default:
                        break;
                }
            }
        });

        updateViews();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (vaccine.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide the required information", Toast.LENGTH_SHORT).show();
                } else if (vaccine.equals("Not Vaccinated")){
                    saveData();
                }else {
                    if(checkIfValidAndRead()){
                        saveData();
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Breed());
                fr.commit();
            }
        });

        return view;
    }

    private void addView() {
        View dewormerView = getLayoutInflater().inflate(R.layout.dewormer, null, false);
        TextView txtDate = dewormerView.findViewById(R.id.date);
        TextView txtWhen = dewormerView.findViewById(R.id.when);
        TextView txtUsed = dewormerView.findViewById(R.id.used);
        EditText etMedication = dewormerView.findViewById(R.id.medication);
        Button datePickerBtn = dewormerView.findViewById(R.id.datepicker);
        ImageView close = dewormerView.findViewById(R.id.close);

        txtWhen.setText("When was the vaccination exercise carried out?");
        txtUsed.setText("What vaccine was used?");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(dewormerView);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        linearLayout.addView(dewormerView);
    }

    private void removeView(View dewormerView) {
        linearLayout.removeView(dewormerView);
    }

    private boolean checkIfValidAndRead() {
        dewomerList.clear();
        boolean result = true;

        for (int i=0; i<linearLayout.getChildCount(); i++){
            View dewormerView = linearLayout.getChildAt(i);
            TextView txtDate2 = dewormerView.findViewById(R.id.date);
            EditText etMedication2 = dewormerView.findViewById(R.id.medication);

            Dewormer dewormer = new Dewormer();

            if(!etMedication2.getText().toString().isEmpty()){
                dewormer.setMedication(etMedication2.getText().toString());
            }else {
                result = false;
                break;
            }

            if (!txtDate2.getText().toString().isEmpty()){
                String val = txtDate2.getText().toString();
                dewormer.setDate1(val);
                dewormer.setDate2(convertDate(val));
            }else {
                result = false;
                break;
            }

            dewomerList.add(dewormer);
        }

        if (dewomerList.size() == 0) {
            result = false;
            Toast.makeText(getActivity(), "Add vaccine first!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(getActivity(), "Enter All details correctly", Toast.LENGTH_SHORT).show();
        }else {
            Gson gson = new Gson();

            String json = gson.toJson(dewomerList);
            editor2.putString(VACCINE, json);
            editor2.apply();
        }

        return result;
    }

    private String convertDate(String calDate){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date_2 = "";
        try {
            Date date = format.parse(calDate);
            SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            date_2 = df.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date_2;
    }

    private void saveData() {
        editor2.putString(VACCINATION, vaccine);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Deworming());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        vaccine = sharedPreferences2.getString(VACCINATION, "");

        Gson gson = new Gson();
        String json = sharedPreferences2.getString(VACCINE, null);
        Type type = new TypeToken<ArrayList<Dewormer>>() {}.getType();
        dewomerList = gson.fromJson(json, type);
        if (dewomerList == null) {
            dewomerList = new ArrayList<>();
        }else {
            for (Dewormer cri: dewomerList){
                View dewormerView = getLayoutInflater().inflate(R.layout.dewormer, null, false);
                TextView txtDate3 = dewormerView.findViewById(R.id.date);
                EditText etMedication3 = dewormerView.findViewById(R.id.medication);
                Button datePickerBtn3 = dewormerView.findViewById(R.id.datepicker);
                ImageView close = dewormerView.findViewById(R.id.close);
                TextView txtWhen = dewormerView.findViewById(R.id.when);
                TextView txtUsed = dewormerView.findViewById(R.id.used);

                txtWhen.setText("When was the vaccination exercise carried out?");
                txtUsed.setText("What vaccine was used?");

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeView(dewormerView);
                    }
                });

                txtDate3.setText(cri.getDate1());
                etMedication3.setText(cri.getMedication());

                datePickerBtn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        year = c.get(Calendar.YEAR); // current year
                        month = c.get(Calendar.MONTH); // current month
                        day = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                        datePickerDialog.show();
                    }
                });

                linearLayout.addView(dewormerView);
            }
        }
    }

    private void updateViews() {
        if (vaccine.equals("Vaccinated")){
            radioButton1.setChecked(true);
            linearLayout2.setVisibility(View.VISIBLE);
        }else if (vaccine.equals("Not Vaccinated")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }

    }

}