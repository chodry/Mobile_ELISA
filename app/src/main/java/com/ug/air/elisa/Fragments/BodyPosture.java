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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.R;


public class BodyPosture extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;
    EditText etOthers, etScore, etOthers2;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    CheckBox leap, stand, recumbent, others, none;
    Boolean check1, check2, check3, check4, check5, check6;
    String other, s, score, temperament, other2;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    public static final String CHECK11X = "check11x";
    public static final String CHECK12X = "check12x";
    public static final String CHECK13X = "check13x";
    public static final String CHECK14X = "check14x";
    public static final String CHECK15X = "check15x";
    public static final String POSTURE = "body_posture";
    public static final String OTHERS2 = "others2";
    public static final String TEMP = "temp";
    public static final String TEMPERAMENT = "temperament";
    public static final String SCORE = "body_condition_score";
    private static final int YES = 0;
    private static final int NO = 1;
    private static final int YESP = 2;
    private static final int NOP = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_body_posture, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        etOthers = view.findViewById(R.id.othersText);
        etOthers2 = view.findViewById(R.id.othersText2);
//        etScore = view.findViewById(R.id.score);
        stand = view.findViewById(R.id.stand);
        leap = view.findViewById(R.id.limp);
        recumbent = view.findViewById(R.id.recumbent);
        others = view.findViewById(R.id.others);
        none = view.findViewById(R.id.none);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.alert);
        radioButton2 = view.findViewById(R.id.docile);
        radioButton3 = view.findViewById(R.id.aggressive);
        radioButton4 = view.findViewById(R.id.other);

        textView.setText("Body Posture and Temperament");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

//        etScore.addTextChangedListener(textWatcher);

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
                    checked(recumbent);
                    checked(others);

                }else {
                    stand.setEnabled(true);
                    leap.setEnabled(true);
                    recumbent.setEnabled(true);
                    others.setEnabled(true);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other = etOthers.getText().toString();
                other2 = etOthers2.getText().toString();
//                score = etScore.getText().toString();

                if (etOthers.getVisibility()==View.VISIBLE && other.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }
                else if (etOthers2.getVisibility()==View.VISIBLE && other2.isEmpty()){
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case YES:
                        temperament = "Alert";
                        etOthers2.setVisibility(View.GONE);
                        etOthers2.setText("");
                        break;
                    case NO:
                        temperament = "Docile";
                        etOthers2.setVisibility(View.GONE);
                        etOthers2.setText("");
                        break;
                    case YESP:
                        temperament = "Aggressive";
                        etOthers2.setVisibility(View.GONE);
                        etOthers2.setText("");
                        break;
                    case NOP:
                        temperament = "Other";
                        etOthers2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
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
            s += "Limping, ";
        }
        if(recumbent.isChecked()){
            s += "Recumbent, ";
        }
        if(none.isChecked()){
            s = "None, ";
        }
        if (!other.isEmpty()){
            s += other + ", ";
        }
        s = s.replaceAll(", $", "");

        if (s.equals("") || temperament.isEmpty()){
            Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
        }
        else {
            saveData();
        }
    }

    public void saveData(){

        if (other2.isEmpty()){
            editor2.putString(TEMPERAMENT, temperament);
        }else {
            editor2.putString(TEMPERAMENT, other2);
        }

        editor2.putString(POSTURE, s);
        editor2.putString(OTHERS2, other);
        editor2.putString(TEMP, other2);
//        editor2.putString(SCORE, score);
        editor2.putBoolean(CHECK11X, stand.isChecked());
        editor2.putBoolean(CHECK12X, leap.isChecked());
        editor2.putBoolean(CHECK13X, recumbent.isChecked());
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
        temperament = sharedPreferences2.getString(TEMPERAMENT, "");
        other2 = sharedPreferences2.getString(TEMP, "");
    }

    private void updateViews() {
        stand.setChecked(check1);
        leap.setChecked(check2);
        recumbent.setChecked(check3);
        none.setChecked(check4);
        others.setChecked(check5);

        if (none.isChecked()){
            checked(leap);
            checked(recumbent);
            checked(stand);
            checked(others);
        }

        if (!other.isEmpty()){
            etOthers.setText(other);
            etOthers.setVisibility(View.VISIBLE);
        }

//        etScore.setText(score);

        if (temperament.equals("Alert")){
            radioButton1.setChecked(true);
        }else if (temperament.equals("Docile")){
            radioButton2.setChecked(true);
        }else if (temperament.equals("Aggressive")){
            radioButton3.setChecked(true);
        }else if (temperament.equals(other2)){
            radioButton4.setChecked(true);
            etOthers2.setText(other2);
            etOthers2.setVisibility(View.VISIBLE);
        }else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
        }
    }

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            score = etScore.getText().toString();
            if (!score.isEmpty()){
                float sco = Float.parseFloat(score);
                if (sco > 5.0){
                    etScore.setError("The score should be below 5.0");
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