package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.PatientSignalement.BREED;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;

public class Breed extends Fragment {

    View view;
    EditText etOthers;
    Button backBtn, nextBtn;
    TextView textView;
    LinearLayout linearLayout1, linearLayout2;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2;
    String other, s, animal, breed, breed2;
    RadioGroup radioGroup;
    int index = 0;
    RadioButton duroc, hampshire, landrace, largeWhite, largeBlack, camborough, ankole, ganda, nyoro, zebu, boran, nsoga, others;
    Boolean check1, check2, check3, check4, check5, check6, check7, check8, check9, check10, check11, check12, check13;
    private static final String SELECT = "selector";
    public static final String BREED1 = "breed1";
    public static final String BREED2 = "breed2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_breed, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        radioGroup = view.findViewById(R.id.radioGroup);
        duroc = view.findViewById(R.id.duroc);
        hampshire = view.findViewById(R.id.hampshire);
        landrace = view.findViewById(R.id.landrace);
        largeWhite = view.findViewById(R.id.large_white);
        largeBlack = view.findViewById(R.id.large_black);
        camborough = view.findViewById(R.id.camborough);
        ankole = view.findViewById(R.id.ankole);
        ganda = view.findViewById(R.id.ganda);
        nyoro = view.findViewById(R.id.nyoro);
        zebu = view.findViewById(R.id.zebu);
        boran = view.findViewById(R.id.boran);
        nsoga = view.findViewById(R.id.nsoga);
        others = view.findViewById(R.id.others);
//        linearLayout1 = view.findViewById(R.id.pig_layout);
//        linearLayout2 = view.findViewById(R.id.cattle_layout);

        textView.setText("Animal breed");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        if (animal.equals("piggery")){
            ankole.setVisibility(View.GONE);
            nsoga.setVisibility(View.GONE);
            zebu.setVisibility(View.GONE);
            nyoro.setVisibility(View.GONE);
            boran.setVisibility(View.GONE);
            ganda.setVisibility(View.GONE);
        }else{
            duroc.setVisibility(View.GONE);
            largeBlack.setVisibility(View.GONE);
            largeWhite.setVisibility(View.GONE);
            landrace.setVisibility(View.GONE);
            camborough.setVisibility(View.GONE);
            hampshire.setVisibility(View.GONE);
        }

        loadData();
        updateViews();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                index = radioGroup.indexOfChild(radioButton);
                RadioButton rb= radioGroup.findViewById(checkedId);
                breed = "" + rb.getText();
                if (rb.getText().equals("Others")){
                    etOthers.setVisibility(View.VISIBLE);
                }else{
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
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }
                else if (breed.isEmpty()){
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
                fr.replace(R.id.fragment_container, new PatientSignalement());
                fr.commit();
            }
        });

        return view;
    }

//    private void checked(CheckBox checkBox){
//        checkBox.setChecked(false);
//        checkBox.setSelected(false);
//        checkBox.setEnabled(false);
//    }
//
//    private void checking(CheckBox checkBox, CheckBox check2, CheckBox check3, CheckBox check4, CheckBox check5, CheckBox check6, CheckBox check7, CheckBox check8, CheckBox check9, CheckBox check10, CheckBox check11, CheckBox check12, CheckBox check13){
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (checkBox.isChecked()){
//                    checked(check2);
//                    checked(check3);
//                    checked(check4);
//                    checked(check5);
//                    checked(check6);
//                    checked(check7);
//                    checked(check8);
//                    checked(check9);
//                    checked(check10);
//                    checked(check11);
//                    checked(check12);
//                    checked(check13);
//                }
//                else {
//                    check2.setEnabled(true);
//                    check3.setEnabled(true);
//                    check4.setEnabled(true);
//                    check5.setEnabled(true);
//                    check6.setEnabled(true);
//                    check7.setEnabled(true);
//                    check8.setEnabled(true);
//                    check9.setEnabled(true);
//                    check10.setEnabled(true);
//                    check11.setEnabled(true);
//                    check13.setEnabled(true);
//                    check12.setEnabled(true);
//                }
//            }
//        });
//    }
//
//    private void checkedList() {
//        s = "";
//
//        if(duroc.isChecked()){
//            s += "Duroc, ";
//        }
//        if(hampshire.isChecked()){
//            s += "Hampshire, ";
//        }
//        if(landrace.isChecked()){
//            s += "Landrace, ";
//        }
//        if(largeBlack.isChecked()){
//            s += "Large Black, ";
//        }
//        if(largeWhite.isChecked()){
//            s += "Large White, ";
//        }
//        if(camborough.isChecked()){
//            s += "Camborough, ";
//        }
//        if(ankole.isChecked()){
//            s += "Ankole, ";
//        }
//        if(ganda.isChecked()){
//            s += "Ganda, ";
//        }
//        if(nyoro.isChecked()){
//            s += "Nyoro, ";
//        }
//        if(nsoga.isChecked()){
//            s = "Nsoga, ";
//        }
//        if(boran.isChecked()){
//            s += "Boran, ";
//        }
//        if(zebu.isChecked()){
//            s = "Zebu, ";
//        }
//        if (!other.isEmpty()){
//            s += other + ", ";
//        }
//        s = s.replaceAll(", $", "");
//
//        if (s.equals("")){
//            Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            saveData();
//        }
//    }
//
    public void saveData(){

        editor2.putString(BREED1, breed);
        editor2.putString(BREED2, other);
        editor2.putInt(SELECT, index);

        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (animal.equals("piggery")){
            fr.replace(R.id.fragment_container, new Deworming());
        }else{
            fr.replace(R.id.fragment_container, new Vaccination());
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        breed = sharedPreferences2.getString(BREED1, "");
        other = sharedPreferences2.getString(BREED2, "");
        index = sharedPreferences2.getInt(SELECT, 0);
    }

    private void updateViews() {
        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }

        radioGroup.check(radioGroup.getChildAt(index).getId());

    }
}