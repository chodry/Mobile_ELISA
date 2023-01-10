package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class Symptoms extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etOthers;
    CheckBox diarrhea, vomit, loss, body, death, saliva, fever, others, none, shivering, abortion;
    Boolean check1, check2, check3, check4, check5, check6, check7, check8, check9, check10, check11;
    String other, s, animal;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHECK1X1 = "check1x1";
    public static final String CHECK2X1= "check2x1";
    public static final String CHECK3X1 = "check3x1";
    public static final String CHECK4X1 = "check4x1";
    public static final String CHECK5X1 = "check5x1";
    public static final String CHECK6X1 = "check6x1";
    public static final String CHECK7X1 = "check7x1";
    public static final String CHECK8X1 = "check8x1";
    public static final String CHECK9X1 = "check9x1";
    public static final String CHECK10X1 = "check10x1";
    public static final String CHECK11X1 = "check11x1";
    public static final String SYMPTOMS = "symptoms";
    public static final String OTHERS3 = "others3";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_symptoms, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        diarrhea = view.findViewById(R.id.diarrhea);
        vomit = view.findViewById(R.id.vomit);
        loss = view.findViewById(R.id.loss);
        body = view.findViewById(R.id.body);
        death = view.findViewById(R.id.death);
        fever = view.findViewById(R.id.fever);
        saliva = view.findViewById(R.id.saliver);
        shivering = view.findViewById(R.id.shivering);
        abortion = view.findViewById(R.id.abortion);
        others = view.findViewById(R.id.others);
        none = view.findViewById(R.id.none);

        textView.setText("Clinical Symptoms");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        if (animal.equals("cattle")){
            saliva.setVisibility(View.VISIBLE);
        }else{
            shivering.setVisibility(View.VISIBLE);
            abortion.setVisibility(View.VISIBLE);
        }

        loadData();
        updateViews();

        others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (others.isChecked()){
                    etOthers.setVisibility(View.VISIBLE);
                }else {
                    etOthers.setVisibility(View.GONE);
                    etOthers.setText("");
                }
            }
        });

        none.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (none.isChecked()){
                    checked(diarrhea);
                    checked(vomit);
                    checked(death);
                    checked(fever);
                    checked(loss);
                    checked(body);
                    checked(others);

                    if (animal.equals("cattle")){
                        checked(saliva);
                    }else{
                        checked(shivering);
                        checked(abortion);
                    }

                }else {
                    diarrhea.setEnabled(true);
                    vomit.setEnabled(true);
                    death.setEnabled(true);
                    loss.setEnabled(true);
                    body.setEnabled(true);
                    fever.setEnabled(true);
                    others.setEnabled(true);
//                    abortion.setEnabled(true);
//                    shivering.setEnabled(true);
                    if (animal.equals("cattle")){
                        saliva.setEnabled(true);
                    }else{
                        shivering.setEnabled(true);
                        abortion.setEnabled(true);
                    }
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other = etOthers.getText().toString();

                if (etOthers.getVisibility()==View.VISIBLE && other.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }
                else{
                    checkedList();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Temperature());
                fr.commit();
            }
        });

        return view;
    }

    private void checked(CheckBox checkBox){
        checkBox.setChecked(false);
        checkBox.setSelected(false);
        checkBox.setEnabled(false);
    }

    private void checkedList() {
        s = "";

        if(diarrhea.isChecked()){
            s += "Diarrhea, ";
        }
        if(vomit.isChecked()){
            s += "Vomiting, ";
        }
        if(loss.isChecked()){
            s += "Loss of appetite, ";
        }
        if(body.isChecked()){
            s += "General Body Weakness, ";
        }
        if(fever.isChecked()){
            s += "Fever, ";
        }
        if(death.isChecked()){
            s += "Sudden death, ";
        }
        if(saliva.isChecked()){
            s += "Excessive salivation, ";
        }
        if(shivering.isChecked()){
            s += "Shivering/ crowding, ";
        }
        if(saliva.isChecked()){
            s += "Abortions, ";
        }
        if(none.isChecked()){
            s = "None, ";
        }
        if (!other.isEmpty()){
            s += other + ", ";
        }
        s = s.replaceAll(", $", "");

        if (s.equals("")){
            Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
        }
        else {
            saveData();
        }
    }

    public void saveData(){

        editor2.putString(SYMPTOMS, s);
        editor2.putString(OTHERS3, other);
        editor2.putBoolean(CHECK1X1, diarrhea.isChecked());
        editor2.putBoolean(CHECK2X1, vomit.isChecked());
        editor2.putBoolean(CHECK3X1, loss.isChecked());
        editor2.putBoolean(CHECK4X1, body.isChecked());
        editor2.putBoolean(CHECK5X1, death.isChecked());

        editor2.putBoolean(CHECK7X1, fever.isChecked());
        editor2.putBoolean(CHECK8X1, none.isChecked());
        editor2.putBoolean(CHECK9X1, others.isChecked());

        if (animal.equals("cattle")){
            editor2.putBoolean(CHECK6X1, saliva.isChecked());
        }else{
            editor2.putBoolean(CHECK10X1, shivering.isChecked());
            editor2.putBoolean(CHECK11X1, abortion.isChecked());
        }

        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Camera());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        check1 = sharedPreferences2.getBoolean(CHECK1X1, false);
        check2 = sharedPreferences2.getBoolean(CHECK2X1, false);
        check3 = sharedPreferences2.getBoolean(CHECK3X1, false);
        check4 = sharedPreferences2.getBoolean(CHECK4X1, false);
        check5 = sharedPreferences2.getBoolean(CHECK5X1, false);

        check7 = sharedPreferences2.getBoolean(CHECK7X1, false);
        check8 = sharedPreferences2.getBoolean(CHECK8X1, false);
        check9 = sharedPreferences2.getBoolean(CHECK9X1, false);
        other = sharedPreferences2.getString(OTHERS3, "");

//        if (animal.equals("cattle")){
        check6 = sharedPreferences2.getBoolean(CHECK6X1, false);
//        }else{
        check10 = sharedPreferences2.getBoolean(CHECK10X1, false);
        check11 = sharedPreferences2.getBoolean(CHECK11X1, false);
//        }
    }

    private void updateViews() {
        diarrhea.setChecked(check1);
        vomit.setChecked(check2);
        loss.setChecked(check3);
        body.setChecked(check4);
        death.setChecked(check5);
        saliva.setChecked(check6);
        shivering.setChecked(check10);
        abortion.setChecked(check11);
        fever.setChecked(check7);
        none.setChecked(check8);
        others.setChecked(check9);

        if (none.isChecked()){
            checked(diarrhea);
            checked(vomit);
            checked(death);
            checked(fever);
            checked(loss);
            checked(body);
            checked(saliva);
            checked(others);
            checked(shivering);
            checked(abortion);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }
    }
}