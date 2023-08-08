package com.gtappdevelopers.firebasestorageimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.RandomAccess;
import java.util.concurrent.Executor;

public class StudentActivity extends AppCompatActivity {
    private TextView textViewDistance;
    private LocationManager locationManager;
    private double targetLatitude = 12.9407985;
    private double targetLongitude = 77.5653937;
    Button next,getTable;

    ListView simpleList;
    CollegeDB db;
    ArrayList<String> subname;
    ArrayList<String> percentage;
    String usn;
    TextView alert;

    public static float dis= 50F;

    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        simpleList =findViewById(R.id.simpleListView);
        db = new CollegeDB(StudentActivity.this,"",null,1);

        usn=LoginActivity.getUSN();
        //Toast.makeText(getApplicationContext(),usn,Toast.LENGTH_SHORT).show();
        subname=db.getStudentSubname(usn);
        percentage=db.getStudentCurretAttendance(usn);
        StudentAdapter c = new StudentAdapter(getApplicationContext(), subname, percentage);
        simpleList.setAdapter(c);

        next=findViewById(R.id.btnScanQR);


        GPSturnon();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Please Verify")
                        .setDescription("User Authentication")
                        .setNegativeButtonText("Cancel")
                        .build();
                getPrompt().authenticate(promptInfo);
            }
        });

        textViewDistance = findViewById(R.id.distance);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(StudentActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(StudentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StudentActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();

                float[] distanceResults = new float[1];
                Location.distanceBetween(currentLatitude, currentLongitude, targetLatitude, targetLongitude, distanceResults);

                float distanceInMeters = distanceResults[0];
                float distanceInKilometers = distanceInMeters / 1000; // Convert to kilometers

                textViewDistance.setText(String.valueOf(distanceInMeters) + " m");
                dis=distanceInMeters;

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){}

            @Override
            public void onProviderEnabled(String provider){}

            @Override
            public void onProviderDisabled(String provider){}
        });
    }

    //finger print...
    public BiometricPrompt getPrompt()
    {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(StudentActivity.this,errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if(dis<200) {
                    Intent intent = new Intent(StudentActivity.this, ScanQRCodeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(StudentActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        };
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,executor,callback);
        return biometricPrompt;
    }

    public void GPSturnon(){

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(StudentActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(StudentActivity.this,REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(this, "GPS is tured on", Toast.LENGTH_SHORT).show();

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS required to be tured on", Toast.LENGTH_SHORT).show();
            }
        }
    }

}