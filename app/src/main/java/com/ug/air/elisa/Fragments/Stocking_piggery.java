package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Fragments.Survey.DISEASE;
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


public class Stocking_piggery extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    Button backBtn, nextBtn;
    EditText etDate;
    LinearLayout linearLayout;
    TextView textView, textView2, textView3;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int YES2 = 0;
    private static final int NO2 = 1;
    private static final int NOT2 = 2;
    String stocking, stock, animal, time, date, date_2;
    Spinner spinner;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String PIGGERY_STOCKING = "piggery_stock_from";
    public static final String PIGGERY_STOCK = "piggery_new_stock";
    public static final String PERIOD_P = "period_p";
    public static final String TIME_P = "time_p";
    ArrayAdapter<CharSequence> adapter;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stocking_piggery, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        textView2 = view.findViewById(R.id.stocking);
//        radioGroup1 = view.findViewById(R.id.radioGroup);
        radioGroup2 = view.findViewById(R.id.radioGroup2);
//        radioButton1 = view.findViewById(R.id.yes);
//        radioButton2 = view.findViewById(R.id.no);
        radioButton4 = view.findViewById(R.id.farm);
        radioButton5 = view.findViewById(R.id.both);
        radioButton3 = view.findViewById(R.id.market);
//        linearLayout = view.findViewById(R.id.stock_from);
        textView3 = view.findViewById(R.id.text2);
        spinner = view.findViewById(R.id.time);
        etDate = view.findViewById(R.id.date);

        textView.setText("Stocking");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

//        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
//        animal = sharedPreferences.getString(ANIMAL, "");
//
//        if (animal.equals("PIGGERY")){
//            textView2.setText("Stocking (Brought in new PIGGERY)");
//            textView3.setText("Where did you buy the PIGGERY from");
//        }else{
//            textView2.setText("Stocking (Brought in new pigs)");
//            textView3.setText("Where did you buy the pigs from");
//        }

        loadData();

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        updateViews();

//        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                View radioButton = radioGroup.findViewById(i);
//                int index = radioGroup.indexOfChild(radioButton);
//
//                switch (index) {
//                    case YES:
//                        stock = "Yes";
//                        linearLayout.setVisibility(View.VISIBLE);
//                        break;
//                    case NO:
//                        stock = "No";
//                        linearLayout.setVisibility(View.GONE);
//                        stocking = "";
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YES2:
                        stocking = "From the market";
                        break;
                    case NO2:
                        stocking = "From the farm";
                        break;
                    case NOT2:
                        stocking = "From both the farm and market";
                        break;
                    default:
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                date = etDate.getText().toString();

                if (date.isEmpty() || time.equals("Select one") || stocking.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }else  {
                    date_2 = date + " " + time + " ago";
                    saveData();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
//                fr.replace(R.id.fragment_container, new Stocking_cattle());
//                fr.commit();

                String disease = sharedPreferences2.getString(DISEASE, "");
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                if (disease.equals("Both")){
                    fr.replace(R.id.fragment_container, new Stocking_cattle());
                }else {
                    fr.replace(R.id.fragment_container, new BioSecurityMeasures());
                }
                fr.commit();
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(PERIOD_P, date);
        editor2.putString(TIME_P, time);

        editor2.putString(PIGGERY_STOCKING, stocking);
        editor2.putString(PIGGERY_STOCK, date_2);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Feeding());
        fr.addToBackStack(null);
        fr.commit();
    }

    public void loadData(){

        date = sharedPreferences2.getString(PERIOD_P, "");
        time = sharedPreferences2.getString(TIME_P, "");
        stocking = sharedPreferences2.getString(PIGGERY_STOCKING, "");
    }

    public void updateViews(){
        if (stocking.equals("From the market")){
            radioButton3.setChecked(true);
        }else if (stocking.equals("From the farm")){
            radioButton4.setChecked(true);
        }else if (stocking.equals("From both the farm and market")){
            radioButton5.setChecked(true);
        }else {
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
        }

        etDate.setText(date);

        if (!time.isEmpty()){
            int position = adapter.getPosition(time);
            spinner.setSelection(position);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        time = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}