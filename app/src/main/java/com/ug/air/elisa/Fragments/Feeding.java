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


public class Feeding extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etOthers;
    CheckBox others, same, left, outside, personal, drink;
    Boolean check1, check2, check3, check4, check5, check6;
    String other, s;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHECK1XX = "check1xx";
    public static final String CHECK2XX = "check2xx";
    public static final String CHECK3XX = "check3xx";
    public static final String CHECK4XX = "check4xx";
    public static final String CHECK5XX = "check5xx";
    public static final String CHECK6XX = "check6xx";
    public static final String FEEDING = "feeding_mechanism";
    public static final String OTHERSXX = "othersxx";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feeding, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        same = view.findViewById(R.id.same);
        outside = view.findViewById(R.id.outside);
        personal = view.findViewById(R.id.personal);
        left = view.findViewById(R.id.left);
        others = view.findViewById(R.id.others);
        drink = view.findViewById(R.id.drink);

        textView.setText("Animal Fedding");

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
                fr.replace(R.id.fragment_container, new Stocking());
                fr.commit();
            }
        });

        return view;
    }

    private void checkedList() {
        s = "";

        if(same.isChecked()){
            s += "Grass on the same farm, ";
        }
        if(outside.isChecked()){
            s += "Grass from outside the farm, ";
        }
        if(personal.isChecked()){
            s += "Personal Leftovers, ";
        }
        if(left.isChecked()){
            s += "Leftovers from outside, ";
        }
        if(drink.isChecked()){
            s = "Same drinking point with other animals, ";
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

        editor2.putString(FEEDING, s);
        editor2.putString(OTHERSXX, other);
        editor2.putBoolean(CHECK1XX, same.isChecked());
        editor2.putBoolean(CHECK2XX, outside.isChecked());
        editor2.putBoolean(CHECK3XX, personal.isChecked());
        editor2.putBoolean(CHECK4XX, left.isChecked());
        editor2.putBoolean(CHECK5XX, drink.isChecked());
        editor2.putBoolean(CHECK6XX, others.isChecked());
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Illness());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        check1 = sharedPreferences2.getBoolean(CHECK1XX, false);
        check2 = sharedPreferences2.getBoolean(CHECK2XX, false);
        check3 = sharedPreferences2.getBoolean(CHECK3XX, false);
        check4 = sharedPreferences2.getBoolean(CHECK4XX, false);
        check5 = sharedPreferences2.getBoolean(CHECK5XX, false);
        check6 = sharedPreferences2.getBoolean(CHECK6XX, false);
        other = sharedPreferences2.getString(OTHERSXX, "");
    }

    private void updateViews() {
        same.setChecked(check1);
        outside.setChecked(check2);
        personal.setChecked(check3);
        left.setChecked(check4);
        drink.setChecked(check5);
        others.setChecked(check6);

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }
    }
}