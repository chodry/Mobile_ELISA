
package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.LoginActivity.TOKEN;
import static com.ug.air.elisa.Activities.WelcomeActivity.PERSON;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.Camera.IMAGE_URL;
import static com.ug.air.elisa.Fragments.GPS.INCOMPLETE_2;
import static com.ug.air.elisa.Fragments.Sample.DATE;
import static com.ug.air.elisa.Fragments.PatientSignalement.MAMMALS;
import static com.ug.air.elisa.Fragments.Sample.INCOMPLETE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.Apis.ApiClient;
import com.ug.air.elisa.Apis.JsonPlaceHolder;
import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.Fragments.FarmerDetails;
import com.ug.air.elisa.Fragments.PatientSignalement;
import com.ug.air.elisa.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormMenuActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String animal, token;
    File[] contents;
    int count, count1, count2, count3 = 0;
    TextView txtSend, txtEdit, txtEdit2;
    List<String> imagesList, items;
    File fileX;
    ImageView imageViewBack;
    JsonPlaceHolder jsonPlaceHolder;
    CardView cardView1, cardView2, cardView3, cardView4, cardView5;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_menu);

        txtSend = findViewById(R.id.sent);
        txtEdit = findViewById(R.id.editformtext);
        txtEdit2 = findViewById(R.id.editformtext2);
        imageViewBack = findViewById(R.id.back);
        cardView1 = findViewById(R.id.farmX);
        cardView2 = findViewById(R.id.farmXX);
        cardView3 = findViewById(R.id.animalX);
        cardView4 = findViewById(R.id.animalXX);
        cardView5 = findViewById(R.id.animalXXX);

        radioGroup = findViewById(R.id.radioGroup);

        jsonPlaceHolder = ApiClient.getClient().create(JsonPlaceHolder.class);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FormMenuActivity.this, HomeActivity.class));
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFS_1, 0);
        editor = sharedPreferences.edit();

        animal = sharedPreferences.getString(ANIMAL, "");

        if (animal.equals("farm")){
            cardView1.setVisibility(View.VISIBLE);
            cardView2.setVisibility(View.VISIBLE);

            cardView3.setVisibility(View.GONE);
            cardView4.setVisibility(View.GONE);
            cardView5.setVisibility(View.GONE);
//            cardView5.setVisibility(View.VISIBLE);
//            getSavedForms2();
        }
        else {
            cardView5.setVisibility(View.VISIBLE);
        }
        getSavedForms();


    }

    public void new_form(View view) {
        if (count2==0){
            Toast.makeText(this, "Please first fill in a form about the farm", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(FormMenuActivity.this, FormActivity.class);
            intent.putExtra("farm", "no");
            startActivity(intent);
        }

    }

    public void new_farm(View view) {
        Intent intent = new Intent(FormMenuActivity.this, FormActivity.class);
        intent.putExtra("farm", "yes");
        startActivity(intent);
    }

    public void edit_form(View view) {
        Intent intent = new Intent(FormMenuActivity.this, ListActivity.class);
        intent.putExtra("farm", "no");
        startActivity(intent);
    }

    public void edit_farm(View view) {
        Intent intent = new Intent(FormMenuActivity.this, ListActivity.class);
        intent.putExtra("farm", "yes");
        startActivity(intent);
    }

    public void send_forms(View view) {
//        Toast.makeText(this, "This feature is currently disabled", Toast.LENGTH_SHORT).show();
        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
        if (src.exists()) {
            File[] contents = src.listFiles();
            if (contents.length != 0) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String name = f.getName().toString();
                        if (!name.equals("shared_prefs.xml") && !name.equals("identity.xml") && !name.startsWith("farm_")){
                            String names = name.replace(".xml", "");
                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                            String incomplete = sharedPreferences2.getString(INCOMPLETE, "");
                            String mammal = sharedPreferences2.getString(MAMMALS, "");

                            if (incomplete.equals("complete")){
//                                String image_urls = sharedPreferences2.getString(IMAGE_URL, "");

//                                imagesList = Arrays.asList(image_urls.split(","));
//                                MultipartBody.Part[] fileUpload = new MultipartBody.Part[imagesList.size()];
//                                for(String url: imagesList){
//                                    Log.d("ELISA", "" + url);
//                                    File file2 = new File(url);
//                                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file2);
//                                    fileUpload[imagesList.indexOf(url)] = MultipartBody.Part.createFormData("files", file2.getPath(), fileBody);
//                                }

                                fileX = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + name);
                                RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), fileX);
                                MultipartBody.Part fileUpload2 = MultipartBody.Part.createFormData("file", fileX.getName() ,filePart);


                                token = sharedPreferences.getString(TOKEN, "");
//                                Call<String> call = jsonPlaceHolder.sendFile("Token " + token, fileUpload, fileUpload2);
                                Call<String> call = jsonPlaceHolder.sendFile("Token " + token, fileUpload2);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (!response.isSuccessful()){
                                            Toast.makeText(FormMenuActivity.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        String value = response.body();
//                                        fileX.delete();
//                                        for(String url: imagesList){
//                                            File file2 = new File(url);
//                                            file2.delete();
//                                        }

                                        Toast.makeText(FormMenuActivity.this, value, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(FormMenuActivity.this, "Something went wrong" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }
                }
            }
        }
    }

    public void send_forms_2(View view) {
//        Toast.makeText(this, "waiting", Toast.LENGTH_SHORT).show();
        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
        if (src.exists()) {
            File[] contents = src.listFiles();
            if (contents.length != 0) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String name = f.getName().toString();
                        if (name.startsWith("farm_")){
                            String names = name.replace(".xml", "");
                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorx = sharedPreferences2.edit();
                            String incomplete = sharedPreferences2.getString(INCOMPLETE_2, "");
                            String sub = sharedPreferences2.getString("submitted", "");

                            if (incomplete.equals("complete") && sub.isEmpty()){
//                            if (incomplete.equals("complete")){
                                fileX = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + name);
                                RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), fileX);
                                MultipartBody.Part fileUpload2 = MultipartBody.Part.createFormData("file", fileX.getName() ,filePart);


                                token = sharedPreferences.getString(TOKEN, "");
                                Call<String> call = jsonPlaceHolder.sendFarmFile("Token " + token, fileUpload2);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (!response.isSuccessful()){
                                            Toast.makeText(FormMenuActivity.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        String value = response.body();
                                        editorx.putString("submitted", "yes");
                                        editorx.apply();
//                                        fileX.delete();

                                        Toast.makeText(FormMenuActivity.this, value, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(FormMenuActivity.this, "Something went wrong" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void getSavedForms() {
        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
        if (src.exists()) {
            File[] contents = src.listFiles();
            if (contents.length != 0) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String name = f.getName().toString();
                        if (!name.equals("shared_prefs.xml") && !name.equals("identity.xml") && !name.startsWith("farm_")){
                            String names = name.replace(".xml", "");
                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                            String mammal = sharedPreferences2.getString(MAMMALS, "");
                            String incomplete = sharedPreferences2.getString(INCOMPLETE, "");
                            if (mammal.equals(animal)){
                                count += 1;
                            }
                            if(incomplete.equals("complete") && mammal.equals(animal)){
                                count1 += 1;
                            }
                        }else if (name.startsWith("farm_")){
                            count2 += 1;
                        }
                    }
                }
                txtSend.setText("Send Forms ("+ count1 + ")");
                txtEdit.setText("Edit Forms ("+ count + ")");
                txtEdit2.setText("Edit Forms ("+ count2 + ")");
            }
        }
    }

//    private void getSavedForms2() {
//        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
//        if (src.exists()) {
//            File[] contents = src.listFiles();
//            if (contents.length != 0) {
//                for (File f : contents) {
//                    if (f.isFile()) {
//                        String name = f.getName().toString();
//                        if (name.startsWith("farm_")){
//                            String names = name.replace(".xml", "");
////                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
//                            count2 += 1;
//                        }
//                    }
//                }
//                txtEdit2.setText("Edit Forms ("+ count2 + ")");
//            }
//        }
//    }
}