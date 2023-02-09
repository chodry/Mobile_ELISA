package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.Context;
import android.content.Intent;
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

import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


public class FarmerDetails extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etName, etVillage, etParish, etSubCounty, etDistrict, etFarmName;
    String name, village, parish, subCounty, district, start, animal, filename, uuid, farm;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2, editor3;
    public static final String NAME = "name";
    public static final String FARM_NAME = "farm_name";
    public static final String SECOND = "second_filename";
    public static final String VILLAGE = "village";
    public static final String PARISH = "parish";
    public static final String SUB_COUNTY = "sub_county";
    public static final String DISTRICT = "district";
    public static final String SPECIAL_UUID = "special_uuid";
    public static final String START_DATE = "start_date";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_farmer_details, container, false);

        nextBtn = view.findViewById(R.id.next);
//        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etName = view.findViewById(R.id.name);
        etVillage = view.findViewById(R.id.village);
        etParish = view.findViewById(R.id.parish);
        etSubCounty = view.findViewById(R.id.sub_county);
        etDistrict = view.findViewById(R.id.district);
        etFarmName = view.findViewById(R.id.farm_name);

//        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
//        animal = sharedPreferences.getString(ANIMAL, "");

        textView.setText("Farmer Details");

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("filename")) {
            filename = intent.getExtras().getString("filename");

            sharedPreferences3 = requireActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
            sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, Context.MODE_PRIVATE);

            editor2 = sharedPreferences2.edit();
            Map<String, ?> all = sharedPreferences3.getAll();
            for (Map.Entry<String, ?> x : all.entrySet()) {
                if (x.getValue().getClass().equals(String.class))  editor2.putString(x.getKey(),  (String)x.getValue());
                if (x.getValue().getClass().equals(Boolean.class))  editor2.putBoolean(x.getKey(),  (Boolean) x.getValue());
            }
            editor2.commit();
            editor2.apply();

            filename = filename + ".xml";
            File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + filename);
            if (src.exists()){
                src.delete();
            }

        }else {
            sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
            editor2 = sharedPreferences2.edit();
        }

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
                farm = etFarmName.getText().toString();

                if (name.isEmpty() || village.isEmpty() || parish.isEmpty() || subCounty.isEmpty() || district.isEmpty() || farm.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveData();
                }

            }
        });

//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
//                fr.replace(R.id.fragment_container, new Survey());
//                fr.commit();
//            }
//        });

        return view;
    }

    private void saveData() {

        if (start.isEmpty()){
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            String formattedDate = df.format(currentTime);
            editor2.putString(START_DATE, formattedDate);
        }

        uuid = sharedPreferences2.getString(SPECIAL_UUID, "");
        if (uuid.isEmpty()){
            uuid = UUID.randomUUID().toString();
            editor2.putString(SPECIAL_UUID, uuid);
        }

        editor2.putString(NAME, name);
        editor2.putString(VILLAGE, village);
        editor2.putString(PARISH, parish);
        editor2.putString(SUB_COUNTY, subCounty);
        editor2.putString(DISTRICT, district);
        editor2.putString(FARM_NAME, farm);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Survey());
        fr.addToBackStack(null);
        fr.commit();

//        Toast.makeText(getActivity(), uuid, Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        name = sharedPreferences2.getString(NAME, "");
        start = sharedPreferences2.getString(START_DATE, "");
        village = sharedPreferences2.getString(VILLAGE, "");
        subCounty = sharedPreferences2.getString(SUB_COUNTY, "");
        parish = sharedPreferences2.getString(PARISH, "");
        district = sharedPreferences2.getString(DISTRICT, "");
        farm = sharedPreferences2.getString(FARM_NAME, "");
    }

    private void updateViews() {
        etName.setText(name);
        etParish.setText(parish);
        etSubCounty.setText(subCounty);
        etVillage.setText(village);
        etDistrict.setText(district);
        etFarmName.setText(farm);
    }
}