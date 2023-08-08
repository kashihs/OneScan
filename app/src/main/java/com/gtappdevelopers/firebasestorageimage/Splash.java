package com.gtappdevelopers.firebasestorageimage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent i = new Intent(Splash.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        },2800);
    }



}