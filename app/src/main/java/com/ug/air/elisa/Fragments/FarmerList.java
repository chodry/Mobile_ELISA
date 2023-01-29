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
import static com.ug.air.elisa.Fragments.GPS.DATE;
import static com.ug.air.elisa.Fragments.GPS.FILENAME;
import static com.ug.air.elisa.Fragments.PatientSignalement.AGE;
import static com.ug.air.elisa.Fragments.PatientSignalement.BREED;
import static com.ug.air.elisa.Fragments.PatientSignalement.GENDER;
import static com.ug.air.elisa.Fragments.PatientSignalement.MAMMALS;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.Adapter.FarmAdapter;
import com.ug.air.elisa.Adapter.FormAdapter;
import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.Models.Farm;
import com.ug.air.elisa.Models.Form;
import com.ug.air.elisa.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FarmerList extends Fragment {

    View view;
    RecyclerView recyclerView;
    FarmAdapter farmAdapter;
    List<Farm> farmList;
    ArrayList<String> files;
    SharedPreferences.Editor editor;
    Button next;
    TextView textView;
    String option;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String FARM = "farm";
    public static final String UUID_SPECIAL = "special_uuid";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_farmer_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        next = view.findViewById(R.id.next);
        textView = view.findViewById(R.id.heading);

        textView.setText("Select Farm");

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (option.isEmpty()){
                    Toast.makeText(getActivity(), "Please select a farm before you continue", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), option, Toast.LENGTH_SHORT).show();
                }

//                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
//                fr.replace(R.id.fragment_container, new PatientSignalement());
//                fr.addToBackStack(null);
//                fr.commit();

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        farmList = new ArrayList<>();
        files = new ArrayList<String>();
        accessSharedFile();

        farmAdapter = new FarmAdapter(farmList, getActivity());
        recyclerView.setAdapter(farmAdapter);

//        farmAdapter.setOnItemClickListener(new FarmAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
////                Farm farm = farmList.get(position);
////                String uuid = farm.getAnimal();
//                Toast.makeText(getActivity(), "uuid", Toast.LENGTH_SHORT).show();
//            }
//        });

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
                        String uuid = sharedPreferences2.getString(SPECIAL_UUID, "");
                        String location = district + "-" + subCounty + "-" + parish + "-" + village;
                        Farm farm = new Farm(farmer, location, dat, uuid, filename, option);
                        farmList.add(farm);
                    }
                }

            }
        }
    }

    private void loadData(){
        option = sharedPreferences2.getString(UUID_SPECIAL, "");
    }
}