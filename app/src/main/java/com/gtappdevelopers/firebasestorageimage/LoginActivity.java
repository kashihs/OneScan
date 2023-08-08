package com.gtappdevelopers.firebasestorageimage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    CollegeDB dbc;
    EditText email,pass;
    Button login_to,signup_to,admin_to;
    Spinner selection;

    private static String n;
    public static String getName(){
        return n;
    }

    private static String m;
    public static String getEmail(){
        return m;
    }

    private static String m2;
    public static String getTname(){
        return m2;
    }

    private static String u;
    public static String getUSN(){
        return u;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        dbc = new CollegeDB(this, "", null, 1);
        email=findViewById(R.id.etEmail);
        pass=findViewById(R.id.etPassword);
        login_to=findViewById(R.id.btnLogin);
        signup_to=findViewById(R.id.btnSignUp);
        selection=findViewById(R.id.spin1);


        admin_to=findViewById(R.id.admin);

        admin_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,AdminActivity.class);
                startActivity(intent);
            }
        });


        login_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString().isEmpty()||pass.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(selection.getSelectedItem().toString().equals("Teacher")){
                        int k=dbc.checkteacher(email.getText().toString(),pass.getText().toString());
                        String s=String.valueOf(k);

                        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                        if(k==1){
                            String eid=email.getText().toString();
                            m=email.getText().toString();
                            m2=dbc.getTeacherName(eid);
                            Intent intent=new Intent(LoginActivity.this,GenerateQRCodeActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(selection.getSelectedItem().toString().equals("Student")){
                        int k1=dbc.checkstudent(email.getText().toString(),pass.getText().toString());
                        String s=String.valueOf(k1);
                        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                        if(k1==1){
                            String eid=email.getText().toString();
                            m=email.getText().toString();
                            n=dbc.getStudentName(eid);
                            u=dbc.getStudentUSN(eid);
                            Intent intent=new Intent(LoginActivity.this,StudentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });

        signup_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,selection.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                //if(selection.getSelectedItem().toString().equals("Teacher")){
                //    Intent intent=new Intent(LoginActivity.this,TeacherSignUpActivity.class);
                //    startActivity(intent);
                //}
                if(selection.getSelectedItem().toString().equals("Student")){
                    Intent intent=new Intent(LoginActivity.this,StudentSignUpActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(LoginActivity.this,TeacherSignUpActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}