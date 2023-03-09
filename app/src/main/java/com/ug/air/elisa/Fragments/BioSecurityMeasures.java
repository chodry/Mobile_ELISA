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
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class BioSecurityMeasures extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etOthers;
    CheckBox fence1, isolation, footwear, foot, others, none, fence2, workers, artificial, bull, separate;
    Boolean check1, check2, check3, check4, check5, check6, check7, check8, check9, check10, check11;
    String other, s;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHECK1X = "check1x";
    public static final String CHECK2X = "check2x";
    public static final String CHECK3X = "check3x";
    public static final String CHECK4X = "check4x";
    public static final String CHECK5X = "check5x";
    public static final String CHECK6X = "check6x";
    public static final String CHECK7X = "check7x";
    public static final String CHECK8X = "check8x";
    public static final String CHECK9X = "check9x";
    public static final String CHECK10X = "check10x";
    public static final String CHECK11X = "check11x";
    public static final String SECURITY = "bio_security_measures";
    public static final String OTHERSX = "othersx";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bio_security_measures, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        isolation = view.findViewById(R.id.isolation);
        footwear = view.findViewById(R.id.footwear);
        foot = view.findViewById(R.id.foot);
        fence1 = view.findViewById(R.id.fencing1);
        fence2 = view.findViewById(R.id.fencing2);

        bull = view.findViewById(R.id.bull);
        workers = view.findViewById(R.id.workers);
        separate = view.findViewById(R.id.separate);
        artificial = view.findViewById(R.id.artificial);

        others = view.findViewById(R.id.others);
        none = view.findViewById(R.id.none);

        textView.setText("Bio-Security Measures");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

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
                    checked(foot);
                    checked(fence1);
                    checked(fence2);
                    checked(isolation);
                    checked(footwear);
                    checked(others);
                    checked(bull);
                    checked(separate);
                    checked(artificial);
                    checked(workers);

                }else {
                    foot.setEnabled(true);
                    fence1.setEnabled(true);
                    footwear.setEnabled(true);
                    isolation.setEnabled(true);
                    others.setEnabled(true);
                    fence2.setEnabled(true);
                    separate.setEnabled(true);
                    artificial.setEnabled(true);
                    workers.setEnabled(true);
                    bull.setEnabled(true);
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
                fr.replace(R.id.fragment_container, new ManagementSystem());
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

        if(foot.isChecked()){
            s += "Foot Bath, ";
        }
        if(footwear.isChecked()){
            s += "Protective footwear, ";
        }
        if(isolation.isChecked()){
            s += "Isolation of incoming animals, ";
        }
        if(fence1.isChecked()){
            s += "Double Fencing, ";
        }
        if(fence2.isChecked()){
            s += "Single Fencing, ";
        }
        if(workers.isChecked()){
            s += "Farm-workers are bio-security aware, ";
        }
        if(separate.isChecked()){
            s += "Separate entry and exit points, ";
        }
        if(artificial.isChecked()){
            s += "Artificial insemination, ";
        }
        if(bull.isChecked()){
            s += "On-farm bull, ";
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

        editor2.putString(SECURITY, s);
        editor2.putString(OTHERSX, other);
        editor2.putBoolean(CHECK1X, fence1.isChecked());
        editor2.putBoolean(CHECK2X, footwear.isChecked());
        editor2.putBoolean(CHECK3X, foot.isChecked());
        editor2.putBoolean(CHECK4X, isolation.isChecked());
        editor2.putBoolean(CHECK5X, none.isChecked());
        editor2.putBoolean(CHECK6X, others.isChecked());
        editor2.putBoolean(CHECK7X, workers.isChecked());
        editor2.putBoolean(CHECK8X, fence2.isChecked());
        editor2.putBoolean(CHECK9X, separate.isChecked());
        editor2.putBoolean(CHECK10X, artificial.isChecked());
        editor2.putBoolean(CHECK11X, bull.isChecked());
        editor2.apply();

//        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
//        fr.replace(R.id.fragment_container, new Stocking_cattle());
//        fr.addToBackStack(null);
//        fr.commit();

//        String disease = sharedPreferences2.getString(DISEASE, "");
        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Feeding());
//        if (disease.equals("Foot and Mouth Disease") || disease.equals("Both")){
//            fr.replace(R.id.fragment_container, new Stocking_cattle());
//        }else{
//            fr.replace(R.id.fragment_container, new Stocking_piggery());
//        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        check1 = sharedPreferences2.getBoolean(CHECK1X, false);
        check2 = sharedPreferences2.getBoolean(CHECK2X, false);
        check3 = sharedPreferences2.getBoolean(CHECK3X, false);
        check4 = sharedPreferences2.getBoolean(CHECK4X, false);
        check5 = sharedPreferences2.getBoolean(CHECK5X, false);
        check6 = sharedPreferences2.getBoolean(CHECK6X, false);
        check7 = sharedPreferences2.getBoolean(CHECK7X, false);
        check8 = sharedPreferences2.getBoolean(CHECK8X, false);
        check9 = sharedPreferences2.getBoolean(CHECK9X, false);
        check10 = sharedPreferences2.getBoolean(CHECK10X, false);
        check11 = sharedPreferences2.getBoolean(CHECK11X, false);
        other = sharedPreferences2.getString(OTHERSX, "");
    }

    private void updateViews() {
        fence1.setChecked(check1);
        footwear.setChecked(check2);
        foot.setChecked(check3);
        isolation.setChecked(check4);
        none.setChecked(check5);
        others.setChecked(check6);

        fence2.setChecked(check8);
        bull.setChecked(check11);
        separate.setChecked(check9);
        workers.setChecked(check7);
        artificial.setChecked(check10);

        if (none.isChecked()){
            checked(foot);
            checked(fence1);
            checked(fence2);
            checked(isolation);
            checked(footwear);
            checked(others);
            checked(bull);
            checked(separate);
            checked(artificial);
            checked(workers);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }
    }
}