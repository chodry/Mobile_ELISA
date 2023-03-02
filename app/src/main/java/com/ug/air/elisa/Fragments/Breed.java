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
    String other, s, animal;
    CheckBox duroc, hampshire, landrace, largeWhite, largeBlack, camborough, ankole, ganda, nyoro, zebu, boran, nsoga, others;
    Boolean check1, check2, check3, check4, check5, check6, check7, check8, check9, check10, check11, check12, check13;
    public static final String CHECK1y1 = "check1y1";
    public static final String CHECK2y1= "check2y1";
    public static final String CHECK3y1 = "check3y1";
    public static final String CHECK4y1 = "check4y1";
    public static final String CHECK5y1 = "check5y1";
    public static final String CHECK6y1 = "check6y1";
    public static final String CHECK7y1 = "check7y1";
    public static final String CHECK8y1 = "check8y1";
    public static final String CHECK9y1 = "check9y1";
    public static final String CHECK10y1 = "check10y1";
    public static final String CHECK11y1 = "check11y1";
    public static final String CHECK12y1 = "check12y1";
    public static final String CHECK13y1 = "check13y1";
    public static final String OTHERSy = "othersY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_breed, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
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
        linearLayout1 = view.findViewById(R.id.pig_layout);
        linearLayout2 = view.findViewById(R.id.cattle_layout);

        textView.setText("Animal breed");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        if (animal.equals("piggery")){
            linearLayout1.setVisibility(View.VISIBLE);
        }else{
            linearLayout2.setVisibility(View.VISIBLE);
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
                fr.replace(R.id.fragment_container, new PatientSignalement());
                fr.commit();
            }
        });

        return view;
    }

    private void checkedList() {
        s = "";

        if(duroc.isChecked()){
            s += "Duroc, ";
        }
        if(hampshire.isChecked()){
            s += "Hampshire, ";
        }
        if(landrace.isChecked()){
            s += "Landrace, ";
        }
        if(largeBlack.isChecked()){
            s += "Large Black, ";
        }
        if(largeWhite.isChecked()){
            s += "Large White, ";
        }
        if(camborough.isChecked()){
            s += "Camborough, ";
        }
        if(ankole.isChecked()){
            s += "Ankole, ";
        }
        if(ganda.isChecked()){
            s += "Ganda, ";
        }
        if(nyoro.isChecked()){
            s += "Nyoro, ";
        }
        if(nsoga.isChecked()){
            s = "Nsoga, ";
        }
        if(boran.isChecked()){
            s += "Boran, ";
        }
        if(zebu.isChecked()){
            s = "Zebu, ";
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

        editor2.putString(BREED, s);
        editor2.putString(OTHERSy, other);
        editor2.putBoolean(CHECK1y1, duroc.isChecked());
        editor2.putBoolean(CHECK2y1, hampshire.isChecked());
        editor2.putBoolean(CHECK3y1, landrace.isChecked());
        editor2.putBoolean(CHECK4y1, largeWhite.isChecked());
        editor2.putBoolean(CHECK5y1, largeBlack.isChecked());
        editor2.putBoolean(CHECK6y1, camborough.isChecked());
        editor2.putBoolean(CHECK7y1, ankole.isChecked());
        editor2.putBoolean(CHECK8y1, ganda.isChecked());
        editor2.putBoolean(CHECK9y1, nyoro.isChecked());
        editor2.putBoolean(CHECK10y1, zebu.isChecked());
        editor2.putBoolean(CHECK11y1, boran.isChecked());
        editor2.putBoolean(CHECK12y1, nsoga.isChecked());
        editor2.putBoolean(CHECK13y1, others.isChecked());

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
        check1 = sharedPreferences2.getBoolean(CHECK1y1, false);
        check2 = sharedPreferences2.getBoolean(CHECK2y1, false);
        check3 = sharedPreferences2.getBoolean(CHECK3y1, false);
        check4 = sharedPreferences2.getBoolean(CHECK4y1, false);
        check5 = sharedPreferences2.getBoolean(CHECK5y1, false);
        check6 = sharedPreferences2.getBoolean(CHECK6y1, false);
        check7 = sharedPreferences2.getBoolean(CHECK7y1, false);
        check8 = sharedPreferences2.getBoolean(CHECK8y1, false);
        check9 = sharedPreferences2.getBoolean(CHECK9y1, false);
        check10 = sharedPreferences2.getBoolean(CHECK10y1, false);
        check11 = sharedPreferences2.getBoolean(CHECK11y1, false);
        check12 = sharedPreferences2.getBoolean(CHECK12y1, false);
        check13 = sharedPreferences2.getBoolean(CHECK13y1, false);
        other = sharedPreferences2.getString(OTHERSy, "");
    }

    private void updateViews() {
        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }

        if (linearLayout1.getVisibility() == View.VISIBLE){
            duroc.setChecked(check1);
            hampshire.setChecked(check2);
            landrace.setChecked(check3);
            largeWhite.setChecked(check4);
            largeBlack.setChecked(check5);
            camborough.setChecked(check6);
        }else if (linearLayout2.getVisibility() == View.VISIBLE){
            ankole.setChecked(check7);
            ganda.setChecked(check8);
            nyoro.setChecked(check9);
            zebu.setChecked(check10);
            boran.setChecked(check11);
            nsoga.setChecked(check12);
        }
        others.setChecked(check13);
    }
}