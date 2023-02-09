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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class Sample extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etOthers, etSample;
    CheckBox blood, swabs, fluid, others, none, whole;
    Boolean check1, check2, check3, check4, check5, check6;
    String other, s, sample_name;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHEC1 = "chec1";
    public static final String CHEC2= "chec2";
    public static final String CHEC3 = "chec3";
    public static final String CHEC4 = "chec4";
    public static final String CHEC5 = "chec5";
    public static final String CHEC6 = "chec6";
    public static final String SAMPLE = "sample";
    public static final String OTHERS4 = "others4";
    public static final String SAMPLE_NAME = "sample_name";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sample, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        blood = view.findViewById(R.id.blood);
        swabs = view.findViewById(R.id.swabs);
        fluid = view.findViewById(R.id.fluid);
        others = view.findViewById(R.id.others);
        none = view.findViewById(R.id.none);
        whole = view.findViewById(R.id.whole);
        etSample = view.findViewById(R.id.sample_name);
        linearLayout = view.findViewById(R.id.linear);

        textView.setText("Clinical Samples");

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
                    checked(swabs);
                    checked(fluid);
                    checked(whole);
                    checked(blood);
                    checked(others);
                    linearLayout.setVisibility(View.GONE);
                    etOthers.setText("");
                    etSample.setText("");

                }else {
                    swabs.setEnabled(true);
                    fluid.setEnabled(true);
                    whole.setEnabled(true);
                    blood.setEnabled(true);
                    others.setEnabled(true);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                other = etOthers.getText().toString();
                sample_name = etSample.getText().toString();

                if (etOthers.getVisibility()==View.VISIBLE && other.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }
                else{
                    checkedList();
//                    if (!none.isChecked()){
//                        Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
//                    }else if (sample_name.isEmpty()){
//                        Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
//                    }else {
//                        checkedList();
//                    }

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Diagnosis());
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

        if(blood.isChecked()){
            s += "Blood serum, ";
        }
        if(swabs.isChecked()){
            s += "Swabs, ";
        }
        if(fluid.isChecked()){
            s += "Fluid from vesicle, ";
        }
        if(whole.isChecked()){
            s += "Whole Blood, ";
        }
        if(none.isChecked()){
            s = "No sample taken, ";
        }
        if (!other.isEmpty()){
            s += other + ", ";
        }
        s = s.replaceAll(", $", "");

        if (s.equals("") || (!s.contains("No sample taken") && sample_name.isEmpty())){
            Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
        }
        else {
            saveData();
        }
    }

    private void saveData() {

        editor2.putString(SAMPLE, s);
        editor2.putString(OTHERS4, other);
        editor2.putString(SAMPLE_NAME, sample_name);
        editor2.putBoolean(CHEC1, blood.isChecked());
        editor2.putBoolean(CHEC2, swabs.isChecked());
        editor2.putBoolean(CHEC3, fluid.isChecked());
        editor2.putBoolean(CHEC4, others.isChecked());
        editor2.putBoolean(CHEC5, whole.isChecked());
        editor2.putBoolean(CHEC6, none.isChecked());
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new GPS());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        check1 = sharedPreferences2.getBoolean(CHEC1, false);
        check2 = sharedPreferences2.getBoolean(CHEC2, false);
        check3 = sharedPreferences2.getBoolean(CHEC3, false);
        check4 = sharedPreferences2.getBoolean(CHEC4, false);
        check5 = sharedPreferences2.getBoolean(CHEC5, false);
        check6 = sharedPreferences2.getBoolean(CHEC6, false);
        other = sharedPreferences2.getString(OTHERS4, "");
        sample_name = sharedPreferences2.getString(SAMPLE_NAME, "");
    }

    private void updateViews() {
        blood.setChecked(check1);
        swabs.setChecked(check2);
        fluid.setChecked(check3);
        others.setChecked(check4);
        whole.setChecked(check5);
        none.setChecked(check6);

        if (none.isChecked()){
            checked(blood);
            checked(swabs);
            checked(fluid);
            checked(whole);
            checked(others);
            linearLayout.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }

        etSample.setText(sample_name);
    }
}