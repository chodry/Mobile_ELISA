package com.ug.air.elisa.Fragments;

import static com.ug.air.elisa.Activities.HomeActivity.ANIMAL;
import static com.ug.air.elisa.Activities.WelcomeActivity.SHARED_PREFS_1;
import static com.ug.air.elisa.Fragments.FarmerDetails.START_DATE;
import static com.ug.air.elisa.Fragments.PatientSignalement.START_DATE_2;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.elisa.Activities.FormMenuActivity;
import com.ug.air.elisa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class GPS extends Fragment implements LocationListener {

    View view;
    Button backBtn, nextBtn, gpsBtn, manualBtn;
    TextView textView, txtLocation1, txtLocation2;
    ProgressBar progressBar;
    EditText etNorth, etEast;
    LinearLayout linearLayoutGPS, linearLayoutManually;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    final String TAG = "GPS";
    String lat, lon, animal, clicked, north, east;
    SharedPreferences sharedPreferences2, sharedPreferences, sharedPreferences3;
    SharedPreferences.Editor editor2, editor3;
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String CLICKED = "manually_or_gps";
    public static final String NORTHINGS = "northings";
    public static final String EASTINGS = "eastings";

    public static final String UNIQUE_2 = "unique_id_2";
    public static final String FILENAME_2 = "filename_2";
    public static final String DATE_2 = "created_on_2";
    public static final String DURATION_2 = "duration_2";
    public static final String INCOMPLETE_2 = "incomplete_2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_g_p_s, container, false);

        nextBtn = view.findViewById(R.id.next);
        backBtn = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.heading);
        txtLocation1 = view.findViewById(R.id.location);
        txtLocation2 = view.findViewById(R.id.location2);
        gpsBtn = view.findViewById(R.id.gps);
        progressBar = view.findViewById(R.id.progress_bar);

        etEast = view.findViewById(R.id.east);
        etNorth = view.findViewById(R.id.north);

        linearLayoutGPS = view.findViewById(R.id.get_gps);
        linearLayoutManually = view.findViewById(R.id.manually);

        manualBtn = view.findViewById(R.id.manual);

        textView.setText("GPS Location");

        sharedPreferences2 = requireActivity().getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        loadData();
        updateViews();

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = "gps";
                linearLayoutManually.setVisibility(View.GONE);
                linearLayoutGPS.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                gpsBtn.setEnabled(false);
                getGeoLocation();
            }
        });

        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = "manually";
                linearLayoutManually.setVisibility(View.VISIBLE);
                linearLayoutGPS.setVisibility(View.GONE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                north = etNorth.getText().toString();
                east = etEast.getText().toString();

                if (clicked.equals("gps")) {
                    if (lon.isEmpty() || lat.isEmpty()){
                        Toast.makeText(getActivity(), "Please provide the location of the farm", Toast.LENGTH_SHORT).show();
                    }else {
                        saveForm();
                    }
                }else if (clicked.equals("manually")) {
                    if (north.isEmpty() || east.isEmpty()){
                        Toast.makeText(getActivity(), "Please provide the location of the farm", Toast.LENGTH_SHORT).show();
                    }else {
                        saveData2();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please provide the location of the farm", Toast.LENGTH_SHORT).show();
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

    private void getGeoLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

//        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();
            getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (permissionsToRequest.size() > 0) {
//                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
//                            ALL_PERMISSIONS_RESULT);
            Log.d(TAG, "Permission requests");
//                    canGetLocation = false;
//                }
//            }

            // get location
            getLocation();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.d(TAG, "getLocation: "+loc);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
            progressBar.setVisibility(View.GONE);
            gpsBtn.setEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

//    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
//        ArrayList result = new ArrayList();
//
//        for (String perm : wanted) {
//            if (!hasPermission(perm)) {
//                result.add(perm);
//            }
//        }
//
//        return result;
//    }

//    private boolean hasPermission(String permission) {
//        if (canAskPermission()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
//            }
//        }
//        return true;
//    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case ALL_PERMISSIONS_RESULT:
//                Log.d(TAG, "onRequestPermissionsResult");
//                for (String perms : permissionsToRequest) {
//                    if (!hasPermission(perms)) {
//                        permissionsRejected.add(perms);
//                    }
//                }
//
//                if (permissionsRejected.size() > 0) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
//                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                requestPermissions(permissionsRejected.toArray(
//                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
//                                            }
//                                        }
//                                    });
//                            return;
//                        }
//                    }
//                } else {
//                    Log.d(TAG, "No rejected permissions.");
//                    canGetLocation = true;
//                    getLocation();
//                }
//                break;
//        }
//    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        txtLocation1.setText("" + loc.getLatitude());
        txtLocation2.setText("" + loc.getLongitude());
        progressBar.setVisibility(View.GONE);
        gpsBtn.setEnabled(true);
        saveData(loc);
//        Toast.makeText(getActivity(), "latitude: " + loc.getLatitude() + " and longitude: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates((LocationListener) getActivity());
        }
    }

    private void saveData(Location loc) {
        lat = ""+loc.getLatitude();
        lon = ""+loc.getLongitude();
        editor2.putString(LATITUDE, lat);
        editor2.putString(LONGITUDE, lon);
        editor2.putString(CLICKED, clicked);
        editor2.apply();
    }

    private void saveData2() {
        editor2.putString(NORTHINGS, north);
        editor2.putString(EASTINGS, east);
        editor2.putString(CLICKED, clicked);
        editor2.apply();

        saveForm();
    }

    private void loadData(){
        lat = sharedPreferences2.getString(LATITUDE, "");
        lon = sharedPreferences2.getString(LONGITUDE, "");
        north = sharedPreferences2.getString(NORTHINGS, "");
        east = sharedPreferences2.getString(EASTINGS, "");
        clicked = sharedPreferences2.getString(CLICKED, "");
    }

    private void updateViews(){
        if (clicked.equals("gps")){
            linearLayoutManually.setVisibility(View.GONE);
            linearLayoutGPS.setVisibility(View.VISIBLE);
            txtLocation1.setText(lat);
            txtLocation2.setText(lon);
        }else if (clicked.equals("manually")) {
            linearLayoutManually.setVisibility(View.VISIBLE);
            linearLayoutGPS.setVisibility(View.GONE);
            etNorth.setText(north);
            etEast.setText(east);
        }else {
            linearLayoutManually.setVisibility(View.GONE);
            linearLayoutGPS.setVisibility(View.GONE);
        }

    }

//    public void saveForm(){
//
//        Date currentTime = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
//        String formattedDate = df.format(currentTime);
//
//        getDuration(currentTime);
//
//        String uniqueID = UUID.randomUUID().toString();
//        String filename = formattedDate + "_" + uniqueID;
//
////        editor2.putString(MAMMALS, animal);
//        editor2.putString(UNIQUE, uniqueID);
//        editor2.putString(DATE, formattedDate);
//        editor2.putString(FILENAME, filename);
//        editor2.putString(INCOMPLETE, "complete");
//        editor2.apply();
//
//        sharedPreferences3 = requireActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
//        editor3 = sharedPreferences3.edit();
//
//        Map<String, ?> all = sharedPreferences2.getAll();
//        for (Map.Entry<String, ?> x : all.entrySet()) {
//            if (x.getValue().getClass().equals(String.class))  editor3.putString(x.getKey(),  (String)x.getValue());
//            if (x.getValue().getClass().equals(Boolean.class))  editor3.putBoolean(x.getKey(),  (Boolean) x.getValue());
//        }
//
//        editor3.commit();
//        editor2.clear();
//        editor2.commit();
//        startActivity(new Intent(getActivity(), FormMenuActivity.class));
//    }
//
//    private void getDuration(Date currentTime) {
//        String initial_date = sharedPreferences2.getString(START_DATE_2, "");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
//        try {
//            Date d1 = format.parse(initial_date);
//
//            long diff = currentTime.getTime() - d1.getTime();//as given
//
//            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
//            String duration = String.valueOf(minutes);
//            editor2.putString(DURATION, duration);
//            editor2.apply();
//            Log.d("Difference in time", "getTimeDifference: " + minutes);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    private void saveForm() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        String formattedDate = df.format(currentTime);

        getDuration(currentTime);

        String uniqueID = UUID.randomUUID().toString();
        String filename = "farm_" + formattedDate + "_" + uniqueID;

        editor2.putString(UNIQUE_2, uniqueID);
        editor2.putString(DATE_2, formattedDate);
        editor2.putString(FILENAME_2, filename);
        editor2.putString(INCOMPLETE_2, "complete");
        editor2.apply();

        sharedPreferences3 = requireActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        editor3 = sharedPreferences3.edit();

        Map<String, ?> all = sharedPreferences2.getAll();
        for (Map.Entry<String, ?> x : all.entrySet()) {
            if (x.getValue().getClass().equals(String.class))  editor3.putString(x.getKey(),  (String)x.getValue());
            if (x.getValue().getClass().equals(Boolean.class))  editor3.putBoolean(x.getKey(),  (Boolean) x.getValue());
        }

        editor3.commit();
        editor2.clear();
        editor2.commit();
        startActivity(new Intent(getActivity(), FormMenuActivity.class));
    }

    private void getDuration(Date currentTime) {
        String initial_date = sharedPreferences2.getString(START_DATE, "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        try {
            Date d1 = format.parse(initial_date);

            long diff = currentTime.getTime() - d1.getTime();//as given

            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            String duration = String.valueOf(minutes);
            editor2.putString(DURATION_2, duration);
            editor2.apply();
            Log.d("Difference in time", "getTimeDifference: " + minutes);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}