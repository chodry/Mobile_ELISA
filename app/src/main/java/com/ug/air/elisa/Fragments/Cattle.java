package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class Cattle extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView, textView2;
    EditText etAnimals, etVaccinated, etInfected, etDead;
    String animals, animal, vaccinated, infected, dead;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
//    public static final String ANIMALS = "total_animals";
//    public static final String VACCINATED = "vaccinated";
//    public static final String INFECTED = "infected";
//    public static final String DEAD = "dead";

    public static final String CATTLE = "total_cattle";
    public static final String CATTLE_VACCINATED = "cattle_vaccinated";
    public static final String CATTLE_INFECTED = "cattle_infected";
    public static final String CATTLE_DEAD = "cattle_dead";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cattle, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        textView2 = view.findViewById(R.id.type);
        etAnimals = view.findViewById(R.id.animals);
        etVaccinated = view.findViewById(R.id.vaccinated);
        etInfected = view.findViewById(R.id.infected);
        etDead = view.findViewById(R.id.dead);

        textView.setText("Farmer Animals");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animals = etAnimals.getText().toString();
                infected = etInfected.getText().toString();
                vaccinated = etVaccinated.getText().toString();
                dead = etDead.getText().toString();

                if (animals.isEmpty() || infected.isEmpty() || vaccinated.isEmpty() || dead.isEmpty()){
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
                fr.replace(R.id.fragment_container, new FarmHistory());
                fr.commit();
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(CATTLE, animals);
        editor2.putString(CATTLE_VACCINATED, vaccinated);
        editor2.putString(CATTLE_INFECTED, infected);
        editor2.putString(CATTLE_DEAD, dead);
        editor2.apply();

        String disease = sharedPreferences2.getString(DISEASE, "");
        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        if (disease.equals("Both")){
            fr.replace(R.id.fragment_container, new Piggery());
        }else{
            fr.replace(R.id.fragment_container, new ManagementSystem());
        }
        fr.addToBackStack(null);
        fr.commit();

    }

    private void loadData() {
        vaccinated = sharedPreferences2.getString(CATTLE_VACCINATED, "");
        animals = sharedPreferences2.getString(CATTLE, "");
        infected = sharedPreferences2.getString(CATTLE_INFECTED, "");
        dead = sharedPreferences2.getString(CATTLE_DEAD, "");
    }

    private void updateViews() {
        etAnimals.setText(animals);
        etVaccinated.setText(vaccinated);
        etInfected.setText(infected);
        etDead.setText(dead);
    }
}