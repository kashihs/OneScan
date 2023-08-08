package com.gtappdevelopers.firebasestorageimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.RandomAccess;

public class CollegeDB extends SQLiteOpenHelper {
    public CollegeDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Test2.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TEACHER(TEACHER_NAME TEXT PRIMARY KEY,T_DEPARTMENT TEXT,T_EMAIL TEXT,T_PASSWORD TEXT);");
        db.execSQL("CREATE TABLE SUBJECT(TEACHER_NAME TEXT REFERENCES TEACHER(TEACHER_NAME),COURSE_NAME TEXT,COURSE_CODE TEXT,TOTAL_CLASSES INT);");
        db.execSQL("CREATE TABLE STUDENT(USN TEXT PRIMARY KEY,STUDENT_NAME TEXT,S_EMAIL TEXT,S_PASSWORD TEXT,S_DEPARTMENT TEXT);");
        db.execSQL("CREATE TABLE ATTENDANCE(USN TEXT REFERENCES STUDENT(USN),SUBJECT_NAME TEXT,TEACHER_NAME TEXT REFERENCES TEACHER(TEACHER_NAME),CURRENT_ATTENDANCE INT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TEACHER");
        db.execSQL("DROP TABLE IF EXISTS SUBJECT");
        db.execSQL("DROP TABLE IF EXISTS STUDENT");
        db.execSQL("DROP TABLE IF EXISTS ATTENDANCE");
        onCreate(db);
    }


    public void insertteacher(String a, String b, String c, String d)
    {
        ContentValues cv = new ContentValues();
        cv.put("TEACHER_NAME",a);
        cv.put("T_DEPARTMENT",b);
        cv.put("T_EMAIL",c);
        cv.put("T_PASSWORD",d);
        this.getWritableDatabase().insert("TEACHER","",cv);
    }

    public void insertsubject(String a, String b, String c, int d)
    {
        ContentValues cv = new ContentValues();
        cv.put("TEACHER_NAME",a);
        cv.put("COURSE_NAME",b);
        cv.put("COURSE_CODE",c);
        cv.put("TOTAL_CLASSES",d);
        this.getWritableDatabase().insert("SUBJECT","",cv);
    }

    public void insertstudent(String a, String b, String c,String d, String e)
    {
        ContentValues cv = new ContentValues();
        cv.put("USN",a);
        cv.put("STUDENT_NAME",b);
        cv.put("S_EMAIL",c);
        cv.put("S_PASSWORD",d);
        cv.put("S_DEPARTMENT",e);
        this.getWritableDatabase().insert("STUDENT","",cv);
    }

    public void insertattendance(String a, String b, String c, int d)
    {
        ContentValues cv = new ContentValues();
        cv.put("USN",a);
        cv.put("SUBJECT_NAME",b);
        cv.put("TEACHER_NAME",c);
        cv.put("CURRENT_ATTENDANCE",d);
        this.getWritableDatabase().insert("ATTENDANCE","",cv);
    }

    public String getStudentName(String k){
        String p = null;
        Cursor c= this.getReadableDatabase().rawQuery("SELECT STUDENT_NAME FROM STUDENT WHERE S_EMAIL"+"=?", new String[] {k});
        while (c.moveToNext()){
            p=c.getString(0);
        }
        return p;
    }

    public ArrayList<String> getTeacherSubject(String k){
        ArrayList<String> p = new ArrayList<>();
        Cursor c= this.getReadableDatabase().rawQuery("SELECT COURSE_NAME FROM SUBJECT WHERE TEACHER_NAME"+"=?", new String[] {k});
        while (c.moveToNext()){
            p.add(c.getString(0));
        }
        return p;
    }

    public String getTeacherName(String k){
        String p = null;
        Cursor c= this.getReadableDatabase().rawQuery("SELECT TEACHER_NAME FROM TEACHER WHERE T_EMAIL"+"=?", new String[] {k});
        while (c.moveToNext()){
            p=c.getString(0);
        }
        return p;
    }

    public String getStudentUSN(String k){
        String p = null;
        Cursor c= this.getReadableDatabase().rawQuery("SELECT USN FROM STUDENT WHERE S_EMAIL"+"=?", new String[] {k});
        while (c.moveToNext()){
            p=c.getString(0);
        }
        return p;
    }

    public int getTotalClasses(String k){
        int p = 0;
        Cursor c= this.getReadableDatabase().rawQuery("SELECT TOTAL_CLASSES FROM SUBJECT WHERE COURSE_NAME"+"=?", new String[] {k});
        while (c.moveToNext()){
            p=c.getInt(0);
        }
        return p;
    }
    public ArrayList<String> getStudentCurretAttendance(String k){
        ArrayList<String> p = new ArrayList<String>();
        Cursor c= this.getReadableDatabase().rawQuery("SELECT CURRENT_ATTENDANCE FROM ATTENDANCE WHERE USN"+"=?", new String[] {k});
        while (c.moveToNext()){
            p.add(c.getString(0));
        }
        return p;
    }
    public ArrayList<String> getStudentSubname(String k){
        ArrayList<String> p = new ArrayList<String>();
        Cursor c= this.getReadableDatabase().rawQuery("SELECT SUBJECT_NAME FROM ATTENDANCE WHERE USN"+"=?", new String[] {k});
        while (c.moveToNext()){
            p.add(c.getString(0));
        }
        return p;
    }

    public int checkstudent(String a,String b){
        Cursor c = this.getReadableDatabase().rawQuery("SELECT S_EMAIL,S_PASSWORD FROM STUDENT", null);
        while(c.moveToNext()){
            if(c.getString(0).equals(a) && c.getString(1).equals(b)) return 1;
        }
        return 0;
    }

    public int checkteacher(String a,String b){
        Cursor c = this.getReadableDatabase().rawQuery("SELECT T_EMAIL,T_PASSWORD FROM TEACHER", null);
        while(c.moveToNext()){
            if(c.getString(0).equals(a) && c.getString(1).equals(b)) return 1;
        }
        return 0;
    }


    public  void updateAttendace(String a,String b)
    {
        this.getWritableDatabase().execSQL("UPDATE ATTENDANCE SET CURRENT_ATTENDANCE = CURRENT_ATTENDANCE + 1 WHERE USN IN ( SELECT USN FROM STUDENT WHERE S_EMAIL = '"+a+"') AND SUBJECT_NAME = '"+b+"';");
    }

    public  void updateTotalAttendace(String a,String b)
    {
        this.getWritableDatabase().execSQL("UPDATE SUBJECT SET TOTAL_CLASSES = TOTAL_CLASSES + 1 WHERE TEACHER_NAME IN ( SELECT TEACHER_NAME FROM TEACHER WHERE T_EMAIL = '"+a+"') AND COURSE_NAME = '"+b+"';");
    }

}