package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
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


public class BodyPosture extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etOthers, etScore;
    CheckBox leap, stand, loss, others, none;
    Boolean check1, check2, check3, check4, check5, check6;
    String other, s, score;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHECK11X = "check11x";
    public static final String CHECK12X = "check12x";
    public static final String CHECK13X = "check13x";
    public static final String CHECK14X = "check14x";
    public static final String CHECK15X = "check15x";
    public static final String POSTURE = "body_posture";
    public static final String OTHERS2 = "others2";
    public static final String SCORE = "body_condition_score";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_body_posture, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        etScore = view.findViewById(R.id.score);
        stand = view.findViewById(R.id.stand);
        leap = view.findViewById(R.id.leap);
        loss = view.findViewById(R.id.loss);
        others = view.findViewById(R.id.others);
        none = view.findViewById(R.id.none);

        textView.setText("Body Posture");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

        etScore.addTextChangedListener(textWatcher);

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

        none.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (none.isChecked()){
                    checked(stand);
                    checked(leap);
                    checked(loss);
                    checked(others);

                }else {
                    stand.setEnabled(true);
                    leap.setEnabled(true);
                    loss.setEnabled(true);
                    others.setEnabled(true);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other = etOthers.getText().toString();
                score = etScore.getText().toString();

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
                fr.replace(R.id.fragment_container, new Illness());
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

    private void checkedList() {
        s = "";

        if(stand.isChecked()){
            s += "Unable to stand, ";
        }
        if(leap.isChecked()){
            s += "Leaping, ";
        }
        if(loss.isChecked()){
            s += "Loss of weight, ";
        }
        if(none.isChecked()){
            s = "None, ";
        }
        if (!other.isEmpty()){
            s += other + ", ";
        }
        s = s.replaceAll(", $", "");

        if (s.equals("") || score.isEmpty()){
            Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
        }
        else {
            saveData();
        }
    }

    public void saveData(){

        editor2.putString(POSTURE, s);
        editor2.putString(OTHERS2, other);
        editor2.putString(SCORE, score);
        editor2.putBoolean(CHECK11X, stand.isChecked());
        editor2.putBoolean(CHECK12X, leap.isChecked());
        editor2.putBoolean(CHECK13X, loss.isChecked());
        editor2.putBoolean(CHECK14X, none.isChecked());
        editor2.putBoolean(CHECK15X, others.isChecked());
        editor2.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Temperature());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void loadData() {
        check1 = sharedPreferences2.getBoolean(CHECK11X, false);
        check2 = sharedPreferences2.getBoolean(CHECK12X, false);
        check3 = sharedPreferences2.getBoolean(CHECK13X, false);
        check4 = sharedPreferences2.getBoolean(CHECK14X, false);
        check5 = sharedPreferences2.getBoolean(CHECK15X, false);
        other = sharedPreferences2.getString(OTHERS2, "");
        score = sharedPreferences2.getString(SCORE, "");
    }

    private void updateViews() {
        stand.setChecked(check1);
        leap.setChecked(check2);
        loss.setChecked(check3);
        none.setChecked(check4);
        others.setChecked(check5);

        if (none.isChecked()){
            checked(leap);
            checked(loss);
            checked(stand);
            checked(others);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }

        etScore.setText(score);
    }

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            score = etScore.getText().toString();
            if (!score.isEmpty()){
                int sco = Integer.parseInt(score);
                if (sco > 5){
                    etScore.setError("The score should be below 5");
                    nextBtn.setEnabled(false);
                }else {
                    nextBtn.setEnabled(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}