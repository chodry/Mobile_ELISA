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

import java.util.ArrayList;
import java.util.List;


public class Symptoms extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etOthers, etLesion;
    CheckBox diarrhea1, diarrhea2, frothing, ulceration, red1, red2, red3, red4, wounds, abortion, shivering, crowding, vesicle1, vesicle2, vesicle3, vesicle4, skin, none, others;
    Boolean check1, check2, check3, check4, check5, check6, check7, check8, check9, check10, check11, check12, check13, check14, check15, check16, check17, check18, check19;
    String other, s, animal, lesion;
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
    public static final String CHECK12X1= "check12x1";
    public static final String CHECK13X1 = "check13x1";
    public static final String CHECK14X1 = "check14x1";
    public static final String CHECK15X1 = "check15x1";
    public static final String CHECK16X1 = "check16x1";
    public static final String CHECK17X1 = "check17x1";
    public static final String CHECK18X1 = "check18x1";
    public static final String CHECK19X1 = "check19x1";
    public static final String SYMPTOMS = "symptoms";
    public static final String OTHERS3 = "others3";
    public static final String LESION = "lesion";
    List<CheckBox> items = new ArrayList<CheckBox>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_symptoms, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        etLesion = view.findViewById(R.id.lesion);
        diarrhea1 = view.findViewById(R.id.diarrhea1);
        diarrhea2 = view.findViewById(R.id.diarrhea2);
        frothing = view.findViewById(R.id.frothing);
        ulceration = view.findViewById(R.id.ulcerations);
        crowding = view.findViewById(R.id.crowding);
        wounds = view.findViewById(R.id.wounds);
        skin = view.findViewById(R.id.skin);
        shivering = view.findViewById(R.id.shivering);
        abortion = view.findViewById(R.id.abortion);
        others = view.findViewById(R.id.others);
        none = view.findViewById(R.id.none);
        red1 = view.findViewById(R.id.red_b);
        red2 = view.findViewById(R.id.red_s);
        red3 = view.findViewById(R.id.red_e);
        red4 = view.findViewById(R.id.red_h);
        vesicle1 = view.findViewById(R.id.vesicle1);
        vesicle2 = view.findViewById(R.id.vesicle2);
        vesicle3 = view.findViewById(R.id.vesicle3);
        vesicle4 = view.findViewById(R.id.vesicle4);

        textView.setText("Clinical Signs and Symptoms");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        if (animal.equals("cattle")){
            skin.setVisibility(View.VISIBLE);
            vesicle2.setVisibility(View.VISIBLE);
            vesicle1.setVisibility(View.VISIBLE);
            vesicle3.setVisibility(View.VISIBLE);
            vesicle4.setVisibility(View.VISIBLE);
            abortion.setVisibility(View.VISIBLE);

        }
        else{
            red1.setVisibility(View.VISIBLE);
            red2.setVisibility(View.VISIBLE);
            red3.setVisibility(View.VISIBLE);
            red4.setVisibility(View.VISIBLE);
            diarrhea1.setVisibility(View.VISIBLE);
            diarrhea2.setVisibility(View.VISIBLE);
            crowding.setVisibility(View.VISIBLE);
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

        skin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (skin.isChecked()){
                    etLesion.setVisibility(View.VISIBLE);
                }else {
                    etLesion.setVisibility(View.GONE);
                    etLesion.setText("");
                }
            }
        });

        none.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (none.isChecked()){
                    checked(diarrhea1);
                    checked(diarrhea2);
                    checked(ulceration);
                    checked(shivering);
                    checked(wounds);
                    checked(frothing);
                    checked(abortion);
                    checked(others);
                    checked(crowding);
                    checked(skin);
                    checked(red1);
                    checked(red2);
                    checked(red3);
                    checked(red4);
                    checked(vesicle1);
                    checked(vesicle2);
                    checked(vesicle3);
                    checked(vesicle4);

                }
                else {
                    diarrhea1.setEnabled(true);
                    diarrhea2.setEnabled(true);
                    wounds.setEnabled(true);
                    frothing.setEnabled(true);
                    crowding.setEnabled(true);
                    skin.setEnabled(true);
                    others.setEnabled(true);
                    shivering.setEnabled(true);
                    abortion.setEnabled(true);
                    red1.setEnabled(true);
                    red2.setEnabled(true);
                    red3.setEnabled(true);
                    red4.setEnabled(true);
                    vesicle1.setEnabled(true);
                    vesicle2.setEnabled(true);
                    vesicle3.setEnabled(true);
                    vesicle4.setEnabled(true);
                    ulceration.setEnabled(true);

                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other = etOthers.getText().toString();
                lesion = etLesion.getText().toString();

                if (etOthers.getVisibility()==View.VISIBLE && other.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }
                else if (etLesion.getVisibility()==View.VISIBLE && lesion.isEmpty()){
                    Toast.makeText(getActivity(), "Please provid all the required information", Toast.LENGTH_SHORT).show();
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

    private void checking(CheckBox checkBox){
        items.add(checkBox);
    }

    private void checkedList() {
        s = "";

        items.clear();

        checking(diarrhea1);
        checking(diarrhea2);
        checking(crowding);
        checking(ulceration);
        checking(frothing);
        checking(wounds);
        checking(skin);
        checking(shivering);
        checking(red4);
        checking(red3);
        checking(red2);
        checking(red1);
        checking(abortion);
        checking(vesicle1);
        checking(vesicle3);
        checking(vesicle2);
        checking(vesicle4);
        checking(none);
        checking(others);

        StringBuilder myString = new StringBuilder();
        for (CheckBox item : items){
            if(item.isChecked()) {
                String oth = item.getText().toString();
                if (!oth.equals("Others")){
                    myString.append(item.getText().toString() + ", ");
                }
            }
        }
        s = myString.toString();

        if (!other.isEmpty()){
            s += other + ", ";
        }

        s = s.replaceAll(", $", "");

        if (s.equals("")){
            Toast.makeText(getActivity(), "Please provi all the required information", Toast.LENGTH_SHORT).show();
        }
        else {
            saveData();
        }
    }

    public void saveData(){

        editor2.putString(SYMPTOMS, s);
        editor2.putString(OTHERS3, other);
        editor2.putString(LESION, lesion);
        editor2.putBoolean(CHECK1X1, diarrhea1.isChecked());
        editor2.putBoolean(CHECK2X1, diarrhea2.isChecked());
        editor2.putBoolean(CHECK3X1, frothing.isChecked());
        editor2.putBoolean(CHECK4X1, ulceration.isChecked());
        editor2.putBoolean(CHECK5X1, wounds.isChecked());
        editor2.putBoolean(CHECK6X1, crowding.isChecked());
        editor2.putBoolean(CHECK7X1, shivering.isChecked());
        editor2.putBoolean(CHECK8X1, skin.isChecked());
        editor2.putBoolean(CHECK9X1, red1.isChecked());
        editor2.putBoolean(CHECK10X1, red2.isChecked());
        editor2.putBoolean(CHECK11X1, red3.isChecked());
        editor2.putBoolean(CHECK12X1, red4.isChecked());
        editor2.putBoolean(CHECK13X1, abortion.isChecked());
        editor2.putBoolean(CHECK14X1, vesicle1.isChecked());
        editor2.putBoolean(CHECK15X1, vesicle2.isChecked());
        editor2.putBoolean(CHECK16X1, vesicle3.isChecked());
        editor2.putBoolean(CHECK17X1, vesicle4.isChecked());
        editor2.putBoolean(CHECK18X1, none.isChecked());
        editor2.putBoolean(CHECK19X1, others.isChecked());


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
        check6 = sharedPreferences2.getBoolean(CHECK6X1, false);
        check7 = sharedPreferences2.getBoolean(CHECK7X1, false);
        check8 = sharedPreferences2.getBoolean(CHECK8X1, false);
        check9 = sharedPreferences2.getBoolean(CHECK9X1, false);
        check10 = sharedPreferences2.getBoolean(CHECK10X1, false);
        check11 = sharedPreferences2.getBoolean(CHECK11X1, false);
        check12 = sharedPreferences2.getBoolean(CHECK12X1, false);
        check13 = sharedPreferences2.getBoolean(CHECK13X1, false);
        check14 = sharedPreferences2.getBoolean(CHECK14X1, false);
        check15 = sharedPreferences2.getBoolean(CHECK15X1, false);
        check16 = sharedPreferences2.getBoolean(CHECK16X1, false);
        check17 = sharedPreferences2.getBoolean(CHECK17X1, false);
        check18 = sharedPreferences2.getBoolean(CHECK18X1, false);
        check19 = sharedPreferences2.getBoolean(CHECK19X1, false);
        other = sharedPreferences2.getString(OTHERS3, "");
        lesion = sharedPreferences2.getString(LESION, "");
    }

    private void updateViews() {
        diarrhea1.setChecked(check1);
        diarrhea2.setChecked(check2);
        frothing.setChecked(check3);
        ulceration.setChecked(check4);
        wounds.setChecked(check5);
        crowding.setChecked(check6);
        shivering.setChecked(check7);
        skin.setChecked(check8);
        red1.setChecked(check9);
        red2.setChecked(check10);
        red3.setChecked(check11);
        red4.setChecked(check12);
        abortion.setChecked(check13);
        vesicle1.setChecked(check14);
        vesicle2.setChecked(check15);
        vesicle3.setChecked(check16);
        vesicle4.setChecked(check17);
        none.setChecked(check18);
        others.setChecked(check19);

        if (none.isChecked()){
            checked(diarrhea1);
            checked(diarrhea2);
            checked(ulceration);
            checked(shivering);
            checked(wounds);
            checked(frothing);
            checked(abortion);
            checked(others);
            checked(crowding);
            checked(skin);
            checked(red1);
            checked(red2);
            checked(red3);
            checked(red4);
            checked(vesicle1);
            checked(vesicle2);
            checked(vesicle3);
            checked(vesicle4);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }

        if (!lesion.isEmpty()){
            etLesion.setText(lesion);
            etLesion.setVisibility(View.VISIBLE);
        }
    }
}