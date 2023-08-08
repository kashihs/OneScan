package com.gtappdevelopers.firebasestorageimage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class TeacherSignUpActivity extends AppCompatActivity {

    EditText tname,temail,tpass;
    Spinner branch;
    Button tsignup;
    CollegeDB dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_signup);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tname=findViewById(R.id.tFullName);
        temail=findViewById(R.id.tEmail);
        tpass=findViewById(R.id.tPassword);
        tsignup=findViewById(R.id.tSignUp);
        branch=findViewById(R.id.tDept);

        dbc = new CollegeDB(this, "", null, 1);

        tsignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String t_name=tname.getText().toString();
                String t_email=temail.getText().toString();
                String t_pass=tpass.getText().toString();

                if(t_name.isEmpty()||t_email.isEmpty()||t_pass.isEmpty()){
                    Toast.makeText(TeacherSignUpActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(t_pass.length()<=5){
                        Toast.makeText(TeacherSignUpActivity.this, "Password length must be 6 or above", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(TeacherSignUpActivity.this,branch.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        dbc.insertteacher(tname.getText().toString(),branch.getSelectedItem().toString(),temail.getText().toString(),tpass.getText().toString());
                        Intent intent=new Intent(TeacherSignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}