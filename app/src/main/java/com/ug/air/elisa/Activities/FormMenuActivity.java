
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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
import java.lang.ref.WeakReference;
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
    int count, count1, count2, count3, count4 = 0;
    TextView txtSend, txtEdit, txtEdit2, txtSubmit, txtSend2;
    Dialog dialog;
    List<String> imagesList, items;
    File fileX;
    ImageView imageViewBack;
    JsonPlaceHolder jsonPlaceHolder;
    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6;
    RadioGroup radioGroup;

    List<String> filenames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_menu);

        txtSend = findViewById(R.id.sent);
        txtSend2 = findViewById(R.id.sentx);
        txtEdit = findViewById(R.id.editformtext);
        txtEdit2 = findViewById(R.id.editformtext2);
        imageViewBack = findViewById(R.id.back);
        cardView1 = findViewById(R.id.farmX);
        cardView6 = findViewById(R.id.farm);
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
            cardView6.setVisibility(View.VISIBLE);
            send_forms_2();
//            getSavedForms2();
        }
        else {
            cardView5.setVisibility(View.VISIBLE);
            send_forms();
        }
        getSavedForms();

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count3 == 0) {
                    if (filenames.size() == 0){
                        Toast.makeText(FormMenuActivity.this, "There is no complete form to submit", Toast.LENGTH_SHORT).show();
                    }else {
                        SendAsyncTask task = new SendAsyncTask(FormMenuActivity.this);
                        task.execute(filenames.size());
                    }
                }else {
                    Toast.makeText(FormMenuActivity.this, "Please first complete and send all the farm forms", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filenames.size() == 0){
                    Toast.makeText(FormMenuActivity.this, "There is no complete form to submit", Toast.LENGTH_SHORT).show();
                }else {
                    SendFarmAsyncTask task = new SendFarmAsyncTask(FormMenuActivity.this);
                    task.execute(filenames.size());
                }
            }
        });

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

    public void send_forms() {
//        Toast.makeText(this, "This feature is currently disabled", Toast.LENGTH_SHORT).show();
        filenames.clear();
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

                            if (incomplete.equals("complete")){
                                filenames.add(name);
                            }

                        }
                    }
                }
            }
        }
    }

    public void send_forms_2() {
        filenames.clear();
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
                            String incomplete = sharedPreferences2.getString(INCOMPLETE_2, "");
                            String sub = sharedPreferences2.getString("submitted", "");

                            if (incomplete.equals("complete") && sub.isEmpty()){
                                filenames.add(name);
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
                            String names = name.replace(".xml", "");
                            SharedPreferences sharedPreferences2 = getSharedPreferences(names, Context.MODE_PRIVATE);
                            String incomplete = sharedPreferences2.getString(INCOMPLETE_2, "");
                            String sub = sharedPreferences2.getString("submitted", "");
                            count2 += 1;
                            if (incomplete.equals("complete") && sub.isEmpty()){
                                count4 += 1;
                            }
                            if (sub.isEmpty()) {
                                count3 += 1;
                            }
                        }
                    }
                }
                txtSend.setText("Send Forms ("+ count1 + ")");
                txtSend2.setText("Send Forms ("+ count4 + ")");
                txtEdit.setText("Edit Forms ("+ count + ")");
                txtEdit2.setText("Edit Forms ("+ count2 + ")");
            }
        }
    }

    private static class SendAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<FormMenuActivity> weakReference;

        SendAsyncTask(FormMenuActivity activity) {
            weakReference = new WeakReference<FormMenuActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.dialog = new Dialog(activity);
            activity.dialog.setContentView(R.layout.sending_message);
            activity.dialog.setCancelable(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            activity.dialog.getWindow().setAttributes(lp);
            lp.gravity = Gravity.CENTER;

            activity.txtSubmit = activity.dialog.findViewById(R.id.message);

            activity.dialog.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return "Nothing";
            }

            for (int i = 0; i < integers[0]; i++){
                String val = activity.filenames.get(i);
                publishProgress(i+1);
                send_data(activity, val);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Forms submitted successfully";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.txtSubmit.setText("Sending animal forms: " + values[0] + " / " + activity.filenames.size());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
//            activity.txtSend.setText("Send Forms (0)");
            activity.filenames.clear();
            activity.dialog.dismiss();
        }

    }

    private static class SendFarmAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<FormMenuActivity> weakReference;

        SendFarmAsyncTask(FormMenuActivity activity) {
            weakReference = new WeakReference<FormMenuActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.dialog = new Dialog(activity);
            activity.dialog.setContentView(R.layout.sending_message);
            activity.dialog.setCancelable(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            activity.dialog.getWindow().setAttributes(lp);
            lp.gravity = Gravity.CENTER;

            activity.txtSubmit = activity.dialog.findViewById(R.id.message);

            activity.dialog.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return "Nothing";
            }

            for (int i = 0; i < integers[0]; i++){
                String val = activity.filenames.get(i);
                publishProgress(i+1);
                send_data_2(activity, val);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Forms submitted successfully";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.txtSubmit.setText("Sending farm forms: " + values[0] + " / " + activity.filenames.size());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            FormMenuActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.filenames.clear();
//            activity.txtSend2.setText("Send Forms (0)");
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.dialog.dismiss();
        }

    }

    public static void send_data(FormMenuActivity activity, String name){
        List<String> imagesList = new ArrayList<>();
        String names = name.replace(".xml", "");
        SharedPreferences sharedPreferences2 = activity.getSharedPreferences(names, Context.MODE_PRIVATE);

        String image_urls = sharedPreferences2.getString(IMAGE_URL, "");

        imagesList = Arrays.asList(image_urls.split(","));
        MultipartBody.Part[] fileUpload = new MultipartBody.Part[imagesList.size()];
        for(String url: imagesList){
            Log.d("ELISA", "" + url);
            File file2 = new File(url);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file2);
            fileUpload[imagesList.indexOf(url)] = MultipartBody.Part.createFormData("files", file2.getPath(), fileBody);
        }

        File fileX = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + name);
        RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), fileX);
        MultipartBody.Part fileUpload2 = MultipartBody.Part.createFormData("file", fileX.getName() ,filePart);

        final List<String> imgList = imagesList;

        activity.token = activity.sharedPreferences.getString(TOKEN, "");
        Call<String> call = activity.jsonPlaceHolder.sendFile("Token " + activity.token, fileUpload, fileUpload2);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    Log.d("Failure message", "onFail: Something went wrong");
                    return;
                }
                String value = response.body();
                Log.d("Success message", "onSuccess: " + value);
                fileX.delete();
                for(String url: imgList){
                    File file2 = new File(url);
                    file2.delete();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error message", "onFailure: " + t.getMessage());
            }
        });

        }

    public static void send_data_2(FormMenuActivity activity, String name){
        String names = name.replace(".xml", "");
        SharedPreferences sharedPreferences2 = activity.getSharedPreferences(names, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorx2 = sharedPreferences2.edit();

        File fileX = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + name);
        RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), fileX);
        MultipartBody.Part fileUpload2 = MultipartBody.Part.createFormData("file", fileX.getName() ,filePart);


        activity.token = activity.sharedPreferences.getString(TOKEN, "");
        Call<String> call = activity.jsonPlaceHolder.sendFarmFile("Token " + activity.token, fileUpload2);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    Log.d("Failure message", "onFail: Something went wrong");
                    return;
                }
                String value = response.body();
                Log.d("Success message", "onSuccess: " + value);
                editorx2.putString("submitted", "yes");
                editorx2.apply();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error message", "onFailure: " + t.getMessage());
            }
        });

    }

}