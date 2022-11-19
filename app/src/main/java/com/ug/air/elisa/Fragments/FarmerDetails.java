package com.ug.air.elisa.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class FarmerDetails extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etName, etVillage, etParish, etSubCounty, etDistrict;
    String name, village, parish, subCounty, district;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    public static final String SHARED_PREFS_2 = "shared_prefs";
    public static final String NAME = "name";
    public static final String VILLAGE = "village";
    public static final String PARISH = "parish";
    public static final String SUB_COUNTY = "subCounty";
    public static final String DISTRICT = "district";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_farmer_details, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etName = view.findViewById(R.id.name);
        etVillage = view.findViewById(R.id.village);
        etParish = view.findViewById(R.id.parish);
        etSubCounty = view.findViewById(R.id.sub_county);
        etDistrict = view.findViewById(R.id.district);

        textView.setText("Farmer Details");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                village = etVillage.getText().toString();
                parish = etParish.getText().toString();
                subCounty = etSubCounty.getText().toString();
                district = etDistrict.getText().toString();

                if (name.isEmpty() || village.isEmpty() || parish.isEmpty() || subCounty.isEmpty() || district.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the data", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveData();
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

        return view;
    }

    private void saveData() {

        editor2.putString(NAME, name);
        editor2.putString(VILLAGE, village);
        editor2.putString(PARISH, parish);
        editor2.putString(SUB_COUNTY, subCounty);
        editor2.putString(DISTRICT, district);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new FarmHistory());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        name = sharedPreferences2.getString(NAME, "");
        village = sharedPreferences2.getString(VILLAGE, "");
        subCounty = sharedPreferences2.getString(SUB_COUNTY, "");
        parish = sharedPreferences2.getString(PARISH, "");
        district = sharedPreferences2.getString(DISTRICT, "");
    }

    private void updateViews() {
        etName.setText(name);
        etParish.setText(parish);
        etSubCounty.setText(subCounty);
        etVillage.setText(village);
        etDistrict.setText(district);
    }
}