package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ug.air.elisa.Adapter.CameraAdapter;
import com.ug.air.elisa.Models.Image;
import com.ug.air.elisa.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Camera extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    Button backBtn, nextBtn, captureBtn, takeBtn, saveBtn, cancelBtn;
    Spinner spinner;
    ImageView imageView, imageView2;
    TextView textView;
    LinearLayout linearLayout;
    Dialog dialog, dialog2;
    EditText etOther;
    String time, animal, other, currentPhotoPath, imagex, imagex2, da1, da2;
    SharedPreferences sharedPreferences2, sharedPreferences;
    SharedPreferences.Editor editor2;
    ArrayAdapter<CharSequence> adapter;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_SYMPTOM = "image_symptom";
    List<String> image_url, image_symptom;
    Gson gson;
    List<Image> imagesList;
    CameraAdapter cameraAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        captureBtn = view.findViewById(R.id.capture);
        recyclerView = view.findViewById(R.id.recyclerview);

        textView.setText("Clinical Symptoms");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_1, 0);
        animal = sharedPreferences.getString(ANIMAL, "");

        image_url = new ArrayList<String>();
        image_symptom = new ArrayList<String>();
        gson = new Gson();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        imagesList = new ArrayList<>();

        da1 = sharedPreferences2.getString(IMAGE_SYMPTOM, "");
        List<String> elephantList = Arrays.asList(da1.split(","));
//        Log.d("ELISA", "" + elephantList);

        da2 = sharedPreferences2.getString(IMAGE_URL, "");
        List<String> elephantList2 = Arrays.asList(da2.split(","));
//        Log.d("ELISA", "" + elephantList2);

        if (elephantList.size() != 1){
            for (String r: elephantList){
                int index = elephantList.indexOf(r);
                String url = elephantList2.get(index);
                imagesList.add(new Image(r, url));
                Log.d("ELISA22", " " + r + "__" + url);
            }

        }

        cameraAdapter = new CameraAdapter(imagesList, getActivity());
        recyclerView.setAdapter(cameraAdapter);

        cameraAdapter.setOnItemClickListener(new CameraAdapter.OnItemClickListener() {
            @Override
            public void onShowClick(int position) {
                Image image = imagesList.get(position);
                showDialog2(image.getImage_url());
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                da1 = sharedPreferences2.getString(IMAGE_SYMPTOM, "");
                da2 = sharedPreferences2.getString(IMAGE_URL, "");

                if (da1.isEmpty() || da2.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
                }else{
                    FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, new Diagnosis());
                    fr.addToBackStack(null);
                    fr.commit();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Symptoms());
                fr.commit();
            }
        });

        return view;
    }

    private void showDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.picture_area);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        takeBtn = dialog.findViewById(R.id.take);
        linearLayout = dialog.findViewById(R.id.region);
        etOther = dialog.findViewById(R.id.othersText);
        spinner = dialog.findViewById(R.id.time);
        imageView = dialog.findViewById(R.id.image);
        saveBtn = dialog.findViewById(R.id.save);
        cancelBtn = dialog.findViewById(R.id.cancel);

        currentPhotoPath = "";

        if (animal.equals("cattle")){
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cattle, android.R.layout.simple_spinner_item);

        }else{
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.pig, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                other = etOther.getText().toString();

                if (time.equals("Select one") || (etOther.getVisibility() == View.VISIBLE && other.isEmpty())){
                    Toast.makeText(getActivity(), "Please provide information about other animals", Toast.LENGTH_SHORT).show();
                }else {
                    if (!other.isEmpty()){
                       time = other;
                    }
                    dispatchTakePictureIntent();
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPhotoPath.isEmpty()){
                    Toast.makeText(getActivity(), "Please take the picture", Toast.LENGTH_SHORT).show();
                }else {
                    String syt = sharedPreferences2.getString(IMAGE_SYMPTOM, "");
                    String syt2 = sharedPreferences2.getString(IMAGE_URL, "");
                    if (syt.isEmpty()){
                        imagex = time;
                    }else {
                        imagex = syt + "," + time;
                    }

                    if (syt2.isEmpty()){
                        imagex2 = currentPhotoPath;
                    }else {
                        imagex2 = syt2 + "," + currentPhotoPath;
                    }

                    editor2.putString(IMAGE_SYMPTOM, imagex);
                    editor2.putString(IMAGE_URL, imagex2);
                    editor2.apply();

                    imagesList.add(new Image(time, currentPhotoPath));
                    cameraAdapter.notifyDataSetChanged();

                    currentPhotoPath = "";

                    dialog.dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        time = adapterView.getItemAtPosition(i).toString();
        if (time.equals("Other")){
            etOther.setVisibility(View.VISIBLE);
        }else{
            etOther.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {

        if (!currentPhotoPath.isEmpty()){
            File f = new File(currentPhotoPath);
            f.delete();
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.ug.air.elisa",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(Uri.fromFile(f));
                linearLayout.setVisibility(View.GONE);
                Log.d("tag", "Absolute URL is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Livestock_" + timeStamp + ".jpg";
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Livestock");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("Livestock", "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + imageFileName);

        currentPhotoPath = file.getAbsolutePath();

        return file;
    }

    private void showDialog2(String image_url) {
        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.picture);
        dialog2.setCancelable(true);
        Window window = dialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        imageView2 = dialog2.findViewById(R.id.image);
        File f = new File(image_url);
        imageView2.setImageURI(Uri.fromFile(f));

        dialog2.show();
    }
}