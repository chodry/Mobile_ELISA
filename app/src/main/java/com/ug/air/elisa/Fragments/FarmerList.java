package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.FarmerDetails.DISTRICT;
import static com.ug.air.elisa.Fragments.FarmerDetails.NAME;
import static com.ug.air.elisa.Fragments.FarmerDetails.PARISH;
import static com.ug.air.elisa.Fragments.FarmerDetails.SPECIAL_UUID;
import static com.ug.air.elisa.Fragments.FarmerDetails.SUB_COUNTY;
import static com.ug.air.elisa.Fragments.FarmerDetails.VILLAGE;
import static com.ug.air.elisa.Fragments.Feeding.DATE_2;
import static com.ug.air.elisa.Fragments.Feeding.FILENAME_2;
import static com.ug.air.elisa.Fragments.Feeding.INCOMPLETE_2;
import static com.ug.air.elisa.Fragments.Sample.DATE;
import static com.ug.air.elisa.Fragments.Sample.FILENAME;
import static com.ug.air.elisa.Fragments.PatientSignalement.AGE;
//import static com.ug.air.elisa.Fragments.PatientSignalement.BREED;
import static com.ug.air.elisa.Fragments.PatientSignalement.GENDER;
import static com.ug.air.elisa.Fragments.PatientSignalement.MAMMALS;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.Adapter.FarmAdapter;
import com.ug.air.elisa.Adapter.FormAdapter;
import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.Models.Farm;
import com.ug.air.elisa.Models.Form;
import com.ug.air.elisa.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class FarmerList extends Fragment {

    View view;
    RecyclerView recyclerView;
    FarmAdapter farmAdapter;
    List<Farm> farmList;
    ArrayList<String> files, items, special;
    SharedPreferences.Editor editor;
    Button next;
    TextView textView;
    RadioGroup radioGroup;
    String option, filename, start;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2;
    public static final String FARM = "farm";
    public static final String UUID_SPECIAL = "uuid_special";
    public static final String START_DATE_2 = "start_date";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_farmer_list, container, false);

        next = view.findViewById(R.id.next);
        textView = view.findViewById(R.id.heading);
        radioGroup = view.findViewById(R.id.radioGroup);

        textView.setText("Select Farm");

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        files = new ArrayList<String>();
        items = new ArrayList<String>();
        special = new ArrayList<String>();
        accessSharedFile();

        for (int i = 0; i < items.size(); i++) {
            RadioButton rb = new RadioButton(getActivity());
            rb.setText(items.get(i));
            rb.setPadding(20, 20, 20, 20);
            radioGroup.addView(rb);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                option = special.get(index);

            }
        });

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
                if (x.getValue().getClass().equals(Integer.class))  editor2.putInt(x.getKey(),  (Integer) x.getValue());
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (option.isEmpty()){
                    Toast.makeText(getActivity(), "Please select a farm before you continue", Toast.LENGTH_SHORT).show();
                }else{
                    saveData();
                }

            }
        });

        return view;
    }

    private void accessSharedFile() {
        files.clear();
        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
        if (src.exists()) {
            File[] contents = src.listFiles();
            if (contents.length != 0) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String name = f.getName().toString();
                        files.add(name);
                    }
                }
                Collections.reverse(files);
                for(String name : files){
                    if (name.startsWith("farm_")){
                        Log.d("ELISA App", "accessSharedFile: "+ name);

                        String names = name.replace(".xml", "");
                        SharedPreferences sharedPreferences2 = requireActivity().getSharedPreferences(names, Context.MODE_PRIVATE);

                        String filename = sharedPreferences2.getString(FILENAME_2, "");
                        String farmer = sharedPreferences2.getString(NAME, "");
                        String village = sharedPreferences2.getString(VILLAGE, "");
                        String subCounty = sharedPreferences2.getString(SUB_COUNTY, "");
                        String parish = sharedPreferences2.getString(PARISH, "");
                        String district = sharedPreferences2.getString(DISTRICT, "");
                        String dat = sharedPreferences2.getString(DATE_2, "");
                        String incomplete = sharedPreferences2.getString(INCOMPLETE_2, "");
                        String uuid = sharedPreferences2.getString(SPECIAL_UUID, "");
                        String location = district + "-" + subCounty + "-" + parish + "-" + village;
                        if (incomplete.equals("complete")){
                            items.add(farmer + "'s farm in " + location);
                            special.add(uuid);
                        }
//                        Farm farm = new Farm(farmer, location, dat, uuid, filename, option);
//                        farmList.add(farm);
                    }
                }

            }
        }
    }

    private void saveData() {
        if (start.isEmpty()){
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            String formattedDate = df.format(currentTime);
            editor2.putString(START_DATE_2, formattedDate);
        }

        editor2.putString(UUID_SPECIAL, option);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new PatientSignalement());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData(){
        option = sharedPreferences2.getString(UUID_SPECIAL, "");
        start = sharedPreferences2.getString(START_DATE_2, "");
//        Toast.makeText(getActivity(), option, Toast.LENGTH_SHORT).show();
    }

    private void updateViews(){

        int rb_index = special.indexOf(option);
        if (rb_index < 0) {
//            Toast.makeText(getActivity(), ""+ rb_index, Toast.LENGTH_SHORT).show();
        }else {
            radioGroup.check(((RadioButton)radioGroup.getChildAt(rb_index)).getId());
        }
//        Toast.makeText(getActivity(), ""+ rb_index, Toast.LENGTH_SHORT).show();
//        radioGroup.check(((RadioButton)radioGroup.getChildAt(rb_index)).getId());

    }

}