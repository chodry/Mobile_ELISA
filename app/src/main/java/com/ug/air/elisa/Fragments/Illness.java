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
import com.ug.air.elisa.Models.Ill;
import com.ug.air.elisa.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Illness extends Fragment {

    View view;
    Button backBtn, nextBtn, addBtn;
    TextView textView;
    EditText etSuffer, etTreat, etDate;
    RadioGroup radioGroup;
    LinearLayout linearLayout, linearLayout2;
    RadioButton radioButton1, radioButton2;
    private static final int YES = 0;
    private static final int NO = 1;
    String illness, suffering, treatment, time, date, date_2;
    Spinner spinner;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    public static final String SUFFERING = "suffering_from";
    public static final String ILLNESS = "previous_illness";
    ArrayList<Ill> dewomerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_illness, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        addBtn = view.findViewById(R.id.add);
        textView = view.findViewById(R.id.heading);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.yes);
        radioButton2 = view.findViewById(R.id.no);
//        etSuffer = view.findViewById(R.id.suffering);
//        etTreat = view.findViewById(R.id.treatment);
        linearLayout = view.findViewById(R.id.layout_list);
        linearLayout2 = view.findViewById(R.id.info);
//        spinner = view.findViewById(R.id.time);
//        etDate = view.findViewById(R.id.date);

        textView.setText("Previous Illness");

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
                        illness = "Yes";
                        linearLayout2.setVisibility(View.VISIBLE);
                        break;
                    case NO:
                        illness = "No";
                        linearLayout2.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (illness.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }
                else if (illness.equals("No")){
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
                fr.replace(R.id.fragment_container, new Deworming());
                fr.commit();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        return view;
    }

    private void addView() {
        View dewormerView = getLayoutInflater().inflate(R.layout.illness, null, false);
        EditText etSuffering = dewormerView.findViewById(R.id.suffering);
        EditText etTreatment = dewormerView.findViewById(R.id.treatment);
        ImageView close = dewormerView.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(dewormerView);
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
            EditText etSuffering = dewormerView.findViewById(R.id.suffering);
            EditText etTreatment = dewormerView.findViewById(R.id.treatment);

            Ill dewormer = new Ill();

            if(!etTreatment.getText().toString().isEmpty()){
                dewormer.setTreatment(etTreatment.getText().toString());
            }else {
                result = false;
                break;
            }

            if (!etSuffering.getText().toString().isEmpty()){
                dewormer.setSuffer(etSuffering.getText().toString());
            }else {
                result = false;
                break;
            }

            dewomerList.add(dewormer);
        }

        if (dewomerList.size() == 0) {
            result = false;
            Toast.makeText(getActivity(), "Add Illness first!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(getActivity(), "Enter All details correctly", Toast.LENGTH_SHORT).show();
        }else {
            Gson gson = new Gson();

            String json = gson.toJson(dewomerList);
            editor2.putString(SUFFERING, json);
            editor2.apply();
        }

        return result;
    }


    private void saveData() {

        editor2.putString(ILLNESS, illness);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new BodyPosture());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        illness = sharedPreferences2.getString(ILLNESS, "");

        Gson gson = new Gson();
        String json = sharedPreferences2.getString(SUFFERING, null);
        Type type = new TypeToken<ArrayList<Ill>>() {}.getType();
        dewomerList = gson.fromJson(json, type);
        if (dewomerList == null) {
            dewomerList = new ArrayList<>();
        }else {
            for (Ill cri: dewomerList){
                View dewormerView = getLayoutInflater().inflate(R.layout.illness, null, false);
                EditText etSuffering = dewormerView.findViewById(R.id.suffering);
                EditText etTreatment = dewormerView.findViewById(R.id.treatment);
                ImageView close = dewormerView.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeView(dewormerView);
                    }
                });

                etSuffering.setText(cri.getSuffer());
                etTreatment.setText(cri.getTreatment());

                linearLayout.addView(dewormerView);
            }
        }
    }

    private void updateViews() {
        if (illness.equals("Yes")){
            radioButton1.setChecked(true);
            linearLayout2.setVisibility(View.VISIBLE);
        }else if (illness.equals("No")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }

    }


}