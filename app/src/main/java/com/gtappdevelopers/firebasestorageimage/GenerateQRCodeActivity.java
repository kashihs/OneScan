package com.gtappdevelopers.firebasestorageimage;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class GenerateQRCodeActivity extends AppCompatActivity{
    TextView qrCodeTV;
    ImageView qrCodeIV;
    EditText dataEdt;
    Button generateQRBtn;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    Spinner sub;
    CollegeDB db;
    String data="kaka";
    String date;
    //ArrayList<String> subjects;
    String k1,k2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        qrCodeTV = findViewById(R.id.idTVgenerateQR) ;
        qrCodeIV = findViewById(R.id.idIVQRCode);
        sub = findViewById(R.id.idTILData); //spinner
        db = new CollegeDB(this, "", null, 1);

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String currentDateAsString = currentDate.format(formatter);
        date=currentDateAsString;


       k1=LoginActivity.getEmail();
       k2=LoginActivity.getTname();
        // Toast.makeText(GenerateQRCodeActivity.this,k2,Toast.LENGTH_SHORT).show();
        List<String> subjects=db.getTeacherSubject(k2);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub.setAdapter(aa);


        generateQRBtn = findViewById(R.id.idBtnGenerateQR);
        generateQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String data = sub.getSelectedItem().toString(); //text we are sending SubjectName+Location
               // Toast.makeText(GenerateQRCodeActivity.this,data,Toast.LENGTH_SHORT).show();
                data = String.valueOf(sub.getSelectedItem())+date; //subject+date
                Toast.makeText(GenerateQRCodeActivity.this,data,Toast.LENGTH_SHORT).show();
                if(data.isEmpty()){
                    Toast.makeText(GenerateQRCodeActivity.this, "Please enter some data to generate qr code",Toast.LENGTH_SHORT).show();
                }else{
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width= point.x;
                    int height= point.y;
                    int dimen= width<height ? width:height;
                    dimen =dimen * 3/4;
                    qrgEncoder = new QRGEncoder(data,null, QRGContents.Type.TEXT, dimen);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrCodeTV.setVisibility(View.GONE);
                        qrCodeIV.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                    }
                db.updateTotalAttendace(LoginActivity.getEmail(),String.valueOf(sub.getSelectedItem()));
                }
            });
        }

    public void updateatdb(){
        String a=LoginActivity.getEmail();

    }
}
