package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Fragments.Survey.DISEASE;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ug.air.elisa.Models.Animal;
import com.ug.air.elisa.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FarmHistory extends Fragment {

    View view;
    Button backBtn, nextBtn, addBtn;
    TextView textView;
    LinearLayout linearLayout;

    ArrayList<Animal> animalList = new ArrayList<>();

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    public static final String FARM_ANIMALS = "farm_animals";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_farm_history, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        addBtn = view.findViewById(R.id.add);
        linearLayout = view.findViewById(R.id.layout_list);

        textView.setText("Farm Details");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();
        
        loadData();
        
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfValidAndRead()){
                    FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, new ManagementSystem());
                    fr.addToBackStack(null);
                    fr.commit();
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

    private void addView() {
        View animalView = getLayoutInflater().inflate(R.layout.total_animals, null, false);
        EditText etAnimal = animalView.findViewById(R.id.animal);
        EditText etTotal = animalView.findViewById(R.id.total);
        ImageView close = animalView.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(animalView);
            }
        });

        linearLayout.addView(animalView);
    }

    private void removeView(View animalView) {
        linearLayout.removeView(animalView);
    }

    private boolean checkIfValidAndRead() {
        animalList.clear();
        boolean result = true;

        for (int i=0; i<linearLayout.getChildCount(); i++){
            View animalView = linearLayout.getChildAt(i);
            EditText etAnimal2 = animalView.findViewById(R.id.animal);
            EditText etTotal2 = animalView.findViewById(R.id.total);

            Animal animal = new Animal();

            if(!etAnimal2.getText().toString().isEmpty()){
                animal.setType(etAnimal2.getText().toString());
            }else {
                result = false;
                break;
            }

            if (!etTotal2.getText().toString().isEmpty()){
                animal.setTotal(etTotal2.getText().toString());
            }else {
                result = false;
                break;
            }

            animalList.add(animal);
        }

        if (animalList.size() == 0) {
            result = false;
            Toast.makeText(getActivity(), "Add animal first!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(getActivity(), "Enter All details correctly", Toast.LENGTH_SHORT).show();
        }else {
            Gson gson = new Gson();

            String json = gson.toJson(animalList);
            editor2.putString(FARM_ANIMALS, json);
            editor2.apply();
        }

        return result;
    }

    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences2.getString(FARM_ANIMALS, null);
        Type type = new TypeToken<ArrayList<Animal>>() {}.getType();
        animalList = gson.fromJson(json, type);
        if (animalList == null) {
            animalList = new ArrayList<>();
        }else {
            for (Animal cri: animalList){
                View animalView = getLayoutInflater().inflate(R.layout.total_animals, null, false);
                EditText etAnimal3 = animalView.findViewById(R.id.animal);
                EditText etTotal3 = animalView.findViewById(R.id.total);
                ImageView close = animalView.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeView(animalView);
                    }
                });

                etAnimal3.setText(cri.getType());
                etTotal3.setText(cri.getTotal());

                linearLayout.addView(animalView);
            }
        }

    }
}