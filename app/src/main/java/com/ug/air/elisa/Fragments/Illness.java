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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class Illness extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etSuffer, etTreat;
    RadioGroup radioGroup;
    LinearLayout linearLayout;
    RadioButton radioButton1, radioButton2;
    private static final int YES = 0;
    private static final int NO = 1;
    String illness, suffering, treatment;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;
    public static final String SUFFERING = "animal_suffering_from";
    public static final String TREATMENT = "treatment_given";
    public static final String ILLNESS = "previous_illness";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_illness, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.yes);
        radioButton2 = view.findViewById(R.id.no);
        etSuffer = view.findViewById(R.id.suffering);
        etTreat = view.findViewById(R.id.treatment);
        linearLayout = view.findViewById(R.id.info);

        textView.setText("Previous Illness");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YES:
                        illness = "Yes";
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case NO:
                        illness = "No";
                        linearLayout.setVisibility(View.GONE);
                        etSuffer.setText("");
                        etTreat.setText("");
                        break;
                    default:
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                treatment = etTreat.getText().toString();
                suffering = etSuffer.getText().toString();

                if (illness.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }else {
                    if (illness.equals("Yes") && (treatment.isEmpty() || suffering.isEmpty())){
                        Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                    }else{
                        saveData();
                    }
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Feeding());
                fr.commit();
            }
        });

        return view;
    }

    private void saveData() {

        editor2.putString(SUFFERING, suffering);
        editor2.putString(TREATMENT, treatment);
        editor2.putString(ILLNESS, illness);
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new BodyPosture());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        suffering = sharedPreferences2.getString(SUFFERING, "");
        treatment = sharedPreferences2.getString(TREATMENT, "");
        illness = sharedPreferences2.getString(ILLNESS, "");
    }

    private void updateViews() {
        if (illness.equals("Yes")){
            radioButton1.setChecked(true);
            linearLayout.setVisibility(View.VISIBLE);
            etTreat.setText(treatment);
            etSuffer.setText(suffering);
        }else if (illness.equals("No")){
            radioButton2.setChecked(true);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
        }


    }
}