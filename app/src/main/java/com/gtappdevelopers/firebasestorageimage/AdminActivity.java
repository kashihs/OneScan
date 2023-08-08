package com.gtappdevelopers.firebasestorageimage;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    EditText editTeacherName;
    EditText editCourseName;
    EditText editCourseCode;
    EditText editTotalClasses;
    EditText editUSN;
    EditText editTextCourseName;
    EditText editTextTeacher;
    EditText editTextCurrentAttendance;

    Button bu1;
    Button bu2;

    CollegeDB dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Open the database
        dbc = new CollegeDB(this, "", null, 1);

        editTeacherName = findViewById(R.id.editTeacherName);
        editCourseName = findViewById(R.id.editCourseName);
        editCourseCode = findViewById(R.id.editCourseCode);
        editTotalClasses = findViewById(R.id.editTotalClasses);
        editUSN = findViewById(R.id.editTextTeacherName);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextTeacher = findViewById(R.id.editTextCourseCode);
        editTextCurrentAttendance = findViewById(R.id.editTextCurrentAttendance);

        bu1 = findViewById(R.id.bu1);
        bu2 = findViewById(R.id.bu2);

        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherName = editTeacherName.getText().toString();
                String courseName = editCourseName.getText().toString();
                String courseCode = editCourseCode.getText().toString();
                //String totalClasses = editTotalClasses.getText().toString();

                dbc.insertsubject(teacherName,courseName,courseCode,0);
            }
        });

        bu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usn = editUSN.getText().toString();
                String courseName = editTextCourseName.getText().toString();
                String tname = editTextTeacher.getText().toString();
                //String currentAttendance = editTextCurrentAttendance.getText().toString();

                dbc.insertattendance(usn,courseName,tname,0);

            }
        });
    }
}