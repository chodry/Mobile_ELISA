
package com.ug.air.elisa.Activities;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.LoginActivity.TOKEN;
import static com.ug.air.elisa.Activities.WelcomeActivity.PERSON;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.Camera.IMAGE_URL;
import static com.ug.air.elisa.Fragments.GPS.MAMMALS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.Apis.ApiClient;
import com.ug.air.elisa.Apis.JsonPlaceHolder;
import com.ug.air.elisa.BuildConfig;
import com.ug.air.elisa.R;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
    int count = 0;
    TextView txtSend;
    List<String> imagesList;
    File fileX;
    ImageView imageViewBack;
    JsonPlaceHolder jsonPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_menu);

        txtSend = findViewById(R.id.sent);
        imageViewBack = findViewById(R.id.back);

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

        getSavedForms();
    }

    public void new_form(View view) {
        startActivity(new Intent(FormMenuActivity.this, FormActivity.class));
    }

    public void edit_form(View view) {
    }

    public void send_forms(View view) {
        File src = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs");
        if (src.exists()) {
            File[] contents = src.listFiles();
            if (contents.length != 0) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String name = f.getName().toString();
                        if (!name.equals("shared_prefs.xml") && !name.equals("identity.xml")){
                            String names = name.replace(".xml", "");
                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                            String image_urls = sharedPreferences2.getString(IMAGE_URL, "");

                            imagesList = Arrays.asList(image_urls.split(","));
                            MultipartBody.Part[] fileUpload = new MultipartBody.Part[imagesList.size()];
                            for(String url: imagesList){
                                Log.d("ELISA", "" + url);
                                File file2 = new File(url);
                                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file2);
                                fileUpload[imagesList.indexOf(url)] = MultipartBody.Part.createFormData("files", file2.getPath(), fileBody);
                            }

                            fileX = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + name);
                            RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), fileX);
                            MultipartBody.Part fileUpload2 = MultipartBody.Part.createFormData("file", fileX.getName() ,filePart);


                            token = sharedPreferences.getString(TOKEN, "");
                            Call<String> call = jsonPlaceHolder.sendFile("Token " + token, fileUpload, fileUpload2);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (!response.isSuccessful()){
                                        Toast.makeText(FormMenuActivity.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String value = response.body();

//                                    fileX.delete();
//                                    for(String url: imagesList){
//                                        File file2 = new File(url);
//                                        file2.delete();
//                                    }

                                    Toast.makeText(FormMenuActivity.this, value, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(FormMenuActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
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
                        if (!name.equals("shared_prefs.xml") && !name.equals("identity.xml")){
                            String names = name.replace(".xml", "");
                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                            String mammal = sharedPreferences2.getString(MAMMALS, "");
                            if (mammal.equals(animal)){
                                count += 1;
                            }
                        }
                    }
                }
                txtSend.setText("Send Forms ("+ count + ")");
            }
        }
    }
}