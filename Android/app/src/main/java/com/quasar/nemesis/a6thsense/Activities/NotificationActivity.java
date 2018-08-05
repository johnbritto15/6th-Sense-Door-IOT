package com.quasar.nemesis.a6thsense.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.quasar.nemesis.a6thsense.R;

import java.io.File;

public class NotificationActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView date,time;
    private SharedPreferences preferences;
    private String secid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        secid=getIntent().getExtras().getString("sec_id","-1");


        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // File galleryFolder = new File(storageDirectory,"6th Sense");
        File subfolder=new File(storageDirectory+ "/" +"6th Sense","Images");
        File file=new File(subfolder+"/"+"image_"+secid+".jpg");
        final Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

        imageView=(ImageView)findViewById(R.id.photo);
        imageView.setImageBitmap(bmp);

        date=(TextView)findViewById(R.id.date);
        preferences=getSharedPreferences("timestamps",MODE_PRIVATE);
        String timest=preferences.getString(secid,"0");
        String[] stamp;
        stamp=timest.split(" ");
        date.setText(String.format("%s%s", date.getText(), stamp[0]));
        time=(TextView)findViewById(R.id.time);
        time.setText(String.format("Time:%s", stamp[1]));
    }
}
