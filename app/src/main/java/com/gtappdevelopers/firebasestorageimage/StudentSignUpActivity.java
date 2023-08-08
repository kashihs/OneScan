package com.gtappdevelopers.firebasestorageimage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StudentSignUpActivity extends AppCompatActivity {

    EditText sname,semail,spass,usn;
    Spinner branch;
    Button ssignup;
    CollegeDB dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sname=findViewById(R.id.stFullName);
        semail=findViewById(R.id.stEmail);
        spass=findViewById(R.id.stPassword);
        usn=findViewById(R.id.stUSN);
        branch=findViewById(R.id.stDept);
        ssignup=findViewById(R.id.stSignUp);

        dbc = new CollegeDB(this, "", null, 1);


        ssignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String s_name=sname.getText().toString();
                String s_email=semail.getText().toString();
                String s_pass=spass.getText().toString();
                String s_usn=usn.getText().toString();
                String dept=branch.getSelectedItem().toString();

                if(s_name.isEmpty()||s_email.isEmpty()||s_pass.isEmpty()||s_usn.isEmpty()){
                    Toast.makeText(StudentSignUpActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(s_pass.length()<=5){
                        Toast.makeText(StudentSignUpActivity.this, "Password length must be 6 or above", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dbc.insertstudent(s_usn,s_name,s_email,s_pass,dept);
                        Intent intent=new Intent(StudentSignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}