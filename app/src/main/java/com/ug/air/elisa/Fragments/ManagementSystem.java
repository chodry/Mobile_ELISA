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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class ManagementSystem extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5, radioButton6;
    String management;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int YES2 = 2;
    private static final int NO2 = 3;
    private static final int YES3 = 4;
    private static final int NO3 = 5;
    public static final String MANAGEMENT = "management";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_management_system, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.intensive);
        radioButton2 = view.findViewById(R.id.extensive);
        radioButton4 = view.findViewById(R.id.communal);
        radioButton5 = view.findViewById(R.id.paddock);
        radioButton6 = view.findViewById(R.id.tethering);
        radioButton3 = view.findViewById(R.id.semi);

        textView.setText("Management System");

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
                        management = "Intensive";
                        break;
                    case NO:
                        management = "Extensive";
                        break;
                    case YES2:
                        management = "Semi-Intensive";
                        break;
                    case NO2:
                        management = "Communal grazing";
                        break;
                    case YES3:
                        management = "Paddock grazing";
                        break;
                    case NO3:
                        management = "Tethering";
                        break;
                    default:
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (management.isEmpty()){
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
                fr.replace(R.id.fragment_container, new Deworming());
                fr.commit();
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(MANAGEMENT, management);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new BioSecurityMeasures());
        fr.addToBackStack(null);
        fr.commit();
    }

    public void loadData(){
        management = sharedPreferences2.getString(MANAGEMENT, "");
    }

    public void updateViews(){
        if (management.equals("Intensive")){
            radioButton1.setChecked(true);
        }else if (management.equals("Extensive")){
            radioButton2.setChecked(true);
        }else if (management.equals("Semi-Intensive")){
            radioButton3.setChecked(true);
        }else if (management.equals("Communal grazing")){
            radioButton4.setChecked(true);
        }else if (management.equals("Paddock grazing")){
            radioButton5.setChecked(true);
        }else if (management.equals("Tethering")){
            radioButton6.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
            radioButton6.setChecked(false);
        }
    }
}