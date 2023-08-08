package com.gtappdevelopers.firebasestorageimage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter{
    Context context;
    String[] subname;
    String[] percentage;
    LayoutInflater inflater;
    CollegeDB db;
    int flag2=0;

    private static int n;
    public static int getNumber(){
        return n;
    }

    public StudentAdapter(Context applicationContext,ArrayList<String> subname,ArrayList<String> percentage){
        this.context = context;
        this.subname = subname.toArray(new String[0]);
        this.percentage = percentage.toArray(new String[0]);

        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return subname.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.studentattendancelist, null);
        db = new CollegeDB(view.getContext(), "",null,1);

        TextView s=(TextView) view.findViewById(R.id.subn);
        TextView pe=(TextView) view.findViewById(R.id.percent);

        double value=Double.valueOf(percentage[i])/db.getTotalClasses(subname[i]);

        s.setText(subname[i]);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if((value*100)<80){
            flag2++;
        }
        if(flag2>0){
            n=3;
        }

        String formattedNumber = decimalFormat.format(value*100);
        pe.setText(formattedNumber+" %");

        //int p=((Integer.valueOf(percentage[i])/db.getTotalClasses(subname[i]))*100);
        //percent.setText(Integer.toString(p)+" %");


        return view;
    }
}