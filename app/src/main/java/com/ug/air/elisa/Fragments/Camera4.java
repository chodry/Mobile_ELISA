package com.ug.air.elisa.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ug.air.elisa.R;


public class Camera4 extends Fragment {

    View view;
    Button backBtn, nextBtn;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera4, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);

        textView.setText("Take Picture");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new GPS());
                fr.addToBackStack(null);
                fr.commit();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Camera3());
                fr.commit();
            }
        });

        return view;
    }
}