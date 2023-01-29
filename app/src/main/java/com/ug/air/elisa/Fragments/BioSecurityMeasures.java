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
    CheckBox fence, isolation, disinfection, foot, others, none;
    Boolean check1, check2, check3, check4, check5, check6;
    String other, s;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHECK1X = "check1x";
    public static final String CHECK2X = "check2x";
    public static final String CHECK3X = "check3x";
    public static final String CHECK4X = "check4x";
    public static final String CHECK5X = "check5x";
    public static final String CHECK6X = "check6x";
    public static final String SECURITY = "security_measures";
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
        disinfection = view.findViewById(R.id.disinfection);
        foot = view.findViewById(R.id.foot);
        fence = view.findViewById(R.id.fencing);
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
                    checked(fence);
                    checked(isolation);
                    checked(disinfection);
                    checked(others);

                }else {
                    foot.setEnabled(true);
                    fence.setEnabled(true);
                    disinfection.setEnabled(true);
                    isolation.setEnabled(true);
                    others.setEnabled(true);
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
        if(disinfection.isChecked()){
            s += "Disinfection, ";
        }
        if(isolation.isChecked()){
            s += "Isolation of new incoming animals, ";
        }
        if(fence.isChecked()){
            s += "Fencing, ";
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
        editor2.putBoolean(CHECK1X, fence.isChecked());
        editor2.putBoolean(CHECK2X, disinfection.isChecked());
        editor2.putBoolean(CHECK3X, foot.isChecked());
        editor2.putBoolean(CHECK4X, isolation.isChecked());
        editor2.putBoolean(CHECK5X, none.isChecked());
        editor2.putBoolean(CHECK6X, others.isChecked());
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Stocking_cattle());
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
        other = sharedPreferences2.getString(OTHERSX, "");
    }

    private void updateViews() {
        fence.setChecked(check1);
        disinfection.setChecked(check2);
        foot.setChecked(check3);
        isolation.setChecked(check4);
        none.setChecked(check5);
        others.setChecked(check6);

        if (none.isChecked()){
            checked(foot);
            checked(fence);
            checked(isolation);
            checked(disinfection);
            checked(others);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }
    }
}