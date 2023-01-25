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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;

public class FarmHistory extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etPeriod, etOthers;
    Spinner spinner;
    CheckBox cattle, pigs, birds, goats, sheep, others, none;
    Boolean check1, check2, check3, check4, check5, check6, check7;
    String time, period, period_2, other, nothing, animal;
    String s = "";
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHECK1 = "check1";
    public static final String CHECK2 = "check2";
    public static final String CHECK3 = "check3";
    public static final String CHECK4 = "check4";
    public static final String CHECK5 = "check5";
    public static final String CHECK6 = "check6";
    public static final String CHECK7 = "check7";
    public static final String FARM = "farm_animals";
    public static final String OTHERS = "others";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_farm_history, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.otherText);
        spinner = view.findViewById(R.id.time);
        cattle = view.findViewById(R.id.cattle);
        pigs = view.findViewById(R.id.pigs);
        birds = view.findViewById(R.id.birds);
        sheep = view.findViewById(R.id.sheep);
        goats = view.findViewById(R.id.goats);
        others = view.findViewById(R.id.others);
        none = view.findViewById(R.id.none);

        textView.setText("Farm Details");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

//        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
//        animal = sharedPreferences.getString(ANIMAL, "");
//
//        if (animal.equals("cattle")){
//            cattle.setVisibility(View.GONE);
//        }else{
//            pigs.setVisibility(View.GONE);
//        }

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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                other = etOthers.getText().toString();

                if (etOthers.getVisibility()==View.VISIBLE && other.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide information about other animals", Toast.LENGTH_SHORT).show();
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
                fr.replace(R.id.fragment_container, new Survey());
                fr.commit();
            }
        });

        none.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (none.isChecked()){
                    checked(cattle);
                    checked(pigs);
                    checked(sheep);
                    checked(birds);
                    checked(goats);
                    checked(others);
                }else {
                    cattle.setEnabled(true);
                    pigs.setEnabled(true);
                    sheep.setEnabled(true);
                    birds.setEnabled(true);
                    others.setEnabled(true);
                    goats.setEnabled(true);
                }
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

        if(cattle.isChecked()){
            s += "Cattle, ";
        }
        if(pigs.isChecked()){
            s += "Pigs, ";
        }
        if(goats.isChecked()){
            s += "Goats, ";
        }
        if(sheep.isChecked()){
            s += "Sheep, ";
        }
        if(birds.isChecked()){
            s = "Birds, ";
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

    private void saveData() {
        editor2.putBoolean(CHECK1, cattle.isChecked());
        editor2.putBoolean(CHECK2, pigs.isChecked());
        editor2.putBoolean(CHECK3, goats.isChecked());
        editor2.putBoolean(CHECK4, sheep.isChecked());
        editor2.putBoolean(CHECK5, birds.isChecked());
        editor2.putBoolean(CHECK6, none.isChecked());
        editor2.putBoolean(CHECK7, others.isChecked());
        editor2.putString(FARM, s);
        editor2.putString(OTHERS, other);
        editor2.apply();

        String disease = sharedPreferences2.getString(DISEASE, "");
        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (disease.equals("Foot and Mouth Disease") || disease.equals("Both")){
            fr.replace(R.id.fragment_container, new Cattle());
        }else{
            fr.replace(R.id.fragment_container, new Piggery());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        check1 = sharedPreferences2.getBoolean(CHECK1, false);
        check2 = sharedPreferences2.getBoolean(CHECK2, false);
        check3 = sharedPreferences2.getBoolean(CHECK3, false);
        check4 = sharedPreferences2.getBoolean(CHECK4, false);
        check5 = sharedPreferences2.getBoolean(CHECK5, false);
        check6 = sharedPreferences2.getBoolean(CHECK6, false);
        check7 = sharedPreferences2.getBoolean(CHECK7, false);
        other = sharedPreferences2.getString(OTHERS, "");
    }

    private void updateViews() {
        cattle.setChecked(check1);
        pigs.setChecked(check2);
        goats.setChecked(check3);
        sheep.setChecked(check4);
        birds.setChecked(check5);
        none.setChecked(check6);
        others.setChecked(check7);

        if (none.isChecked()){
            checked(cattle);
            checked(pigs);
            checked(sheep);
            checked(birds);
            checked(goats);
            checked(others);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }
    }
}