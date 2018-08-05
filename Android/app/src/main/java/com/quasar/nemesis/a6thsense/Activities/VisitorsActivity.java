package com.quasar.nemesis.a6thsense.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.quasar.nemesis.a6thsense.Adapters.RecyclerViewAdapter;
import com.quasar.nemesis.a6thsense.R;

import java.io.File;
import java.util.ArrayList;

public class VisitorsActivity extends AppCompatActivity {
private Bitmap[] bitmaps;
private SharedPreferences sharedPreferences;
private ArrayList<String> val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);

        bitmaps=new Bitmap[10];
        val=new ArrayList<>();
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // File galleryFolder = new File(storageDirectory,"6th Sense");
        File subfolder=new File(storageDirectory+ "/" +"6th Sense","Images");
        for(int i=0;i<10;i++) {
            File file = new File(subfolder + "/" + "image_" + i + ".jpg");
            bitmaps[i] = BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        sharedPreferences=getSharedPreferences("timestamps",MODE_PRIVATE);
        for(int i=0;i<10;i++) {
            val.add(sharedPreferences.getString(Integer.toString(i),"-1"));
        }
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(val,bitmaps);
            RecyclerView myView =  (RecyclerView)findViewById(R.id.recycler);
            myView.setHasFixedSize(true);
            myView.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            myView.setLayoutManager(llm);


        }
    }

