package com.quasar.nemesis.a6thsense.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.widget.FrameLayout;

import com.quasar.nemesis.a6thsense.R;
import com.quasar.nemesis.a6thsense.Adapters.RecyclerViewAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class LoadingActivity extends AppCompatActivity {

    private static final String URL="https://deep-thought-194109.appspot.com/";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Bitmap[] decodedimage=new Bitmap[10];
    private FrameLayout frameLayout;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);
        frameLayout=(FrameLayout)findViewById(R.id.frame);


        new showpastvisitors().execute();
    }

    private class showpastvisitors extends AsyncTask<Void, Void, Bitmap[]> {

        @Override
        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(LoadingActivity.this);
            progressDialog.setMessage("Fetching data...");
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Please wait");
            progressDialog.show();
        }

        @Override
        protected Bitmap[] doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(URL).maxBodySize(0).timeout(0).get();

                Elements divs = doc.getElementsByTag("div");
                int count=0,c=0;
                for (Element div : divs) {

                    Elements s = div.select("input");

                    Elements p = div.select("p");
                    preferences = getSharedPreferences("timestamps", MODE_PRIVATE);  //change this
                    editor = preferences.edit();

                    editor.putString(Integer.toString(c++), p.text());
                    editor.apply();

                    //System.out.println("NETSEC:"+p.text());

                    //  Elements imagetag = doc.getElementsByTag("img");
                    Elements imagetag = div.select("img");
                    String imagedata = imagetag.attr("src");
                    imagedata = imagedata.substring(imagedata.indexOf(",") + 1);
                    //   System.out.println(imagedata);
                    byte[] decode = Base64.decode(imagedata.getBytes(), Base64.DEFAULT);
                    decodedimage[count++] = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                }
                    //   System.out.println(decodedimage.getByteCount());
                    return decodedimage;
                    // Picasso.with(this).load



            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Bitmap[] decodedimage)
        {

            ArrayList<String> val=new ArrayList<>();
           for(int i=0;i<decodedimage.length;i++)
           {
               val.add(preferences.getString(Integer.toString(i),"0"));
           }
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(val,decodedimage);
            RecyclerView myView =  (RecyclerView)findViewById(R.id.recycler);
            myView.setHasFixedSize(true);
            myView.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(LoadingActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            progressDialog.dismiss();
            myView.setLayoutManager(llm);
        }
    }
}
