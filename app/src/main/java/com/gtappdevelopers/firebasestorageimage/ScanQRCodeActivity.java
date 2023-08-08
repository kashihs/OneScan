package com.gtappdevelopers.firebasestorageimage;


import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.CAMERA;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class ScanQRCodeActivity extends AppCompatActivity {
    ScannerLiveView scannerLiveView;
    TextView scannerTV;
    CollegeDB dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        scannerLiveView=findViewById(R.id.camView);
        scannerTV=findViewById(R.id.idTVScannedData);
        dbc = new CollegeDB(this, "", null, 1);

        if(checkPermission()){
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
        }
        else{
            requestPermission();
        }
        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner){
                Toast.makeText(ScanQRCodeActivity.this, "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner){
                Toast.makeText(ScanQRCodeActivity.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err){

            }

            @Override
            public void onCodeScanned(String data){
                scannerTV.setText(data);
                String temp=data.substring(0,3);
                String k=LoginActivity.getEmail();
               // Toast.makeText(ScanQRCodeActivity.this,k,Toast.LENGTH_SHORT).show();
                Toast.makeText(ScanQRCodeActivity.this,temp,Toast.LENGTH_SHORT).show();
                //main heart
                dbc.updateAttendace(k,temp);
                Intent intent=new Intent(ScanQRCodeActivity.this,StudentActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkPermission()
    {
        int camer_permission= ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        int vibrate_permission= ContextCompat.checkSelfPermission(getApplicationContext(),VIBRATE);
        return  camer_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission== PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission(){
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA,VIBRATE},PERMISSION_CODE);
    }
    @Override
    protected void onPause(){
        scannerLiveView.stopScanner();
        super.onPause();
    }
    @Override
    protected void onResume(){

        super.onResume();
        ZXDecoder decoder=new ZXDecoder();
        decoder.setScanAreaPercent(0.8);
        scannerLiveView.setDecoder(decoder);
        scannerLiveView.startScanner();
    }

    public void onRequestPermissionResult(int requestCode, @NonNull  String[] permissions, int [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean camerAccepted= grantResults[0]== PackageManager.PERMISSION_GRANTED;
            boolean vibrationAccepted= grantResults[1]==PackageManager.PERMISSION_GRANTED;
            if(camerAccepted && vibrationAccepted)
            {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied \n you cannot use the app without permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

}