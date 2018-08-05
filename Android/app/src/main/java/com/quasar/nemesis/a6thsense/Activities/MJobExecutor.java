package com.quasar.nemesis.a6thsense.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vipla on 02-03-2018.
 */

public class MJobExecutor extends AsyncTask<Context,Void,ArrayList> {
private String URL="https://deep-thought-194109.appspot.com/";
private SharedPreferences preferences;
private SharedPreferences.Editor editor;
private int c;


    @Override
    protected ArrayList doInBackground(Context... context) {
        Context context1=context[0];
        ArrayList<String> secid = new ArrayList<>();
        c=0;
        try {
            Document doc = Jsoup.connect(URL).maxBodySize(0).timeout(0).get();


            Elements strongs = doc.select("input");
            for (Element s : strongs) {
                System.out.println(s.attr("value"));
                secid.add(s.attr("value"));


            }
            Elements para = doc.select("p");
            for (Element p : para) {
                System.out.println(p.text());
                preferences = context1.getSharedPreferences("timestamps", MODE_PRIVATE);  //change this
                editor = preferences.edit();

                editor.putString(secid.get(c).substring(secid.get(c++).length()-1), p.text());
                editor.apply();
            }

            Elements imagetag = doc.select("img");
            int count=0;
            for(Element i:imagetag)
            {
                String imagedata = i.attr("src");
                imagedata = imagedata.substring(imagedata.indexOf(",") + 1);
                byte[] decode = Base64.decode(imagedata.getBytes(), Base64.DEFAULT);
                Bitmap decodedimage = BitmapFactory.decodeByteArray(decode, 0, decode.length);

                File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File galleryFolder = new File(storageDirectory,"6th Sense");
                if (!galleryFolder.exists()) {
                    boolean wasCreated = galleryFolder.mkdirs();
                    if (!wasCreated) {
                        Log.e("CapturedImages", "Failed to create directory");
                    }
                }
                File subfolder=new File(storageDirectory+ "/" +"6th Sense","Images");
                // System.out.println("Subfolder:"+subfolder);
                if(!subfolder.exists())
                {
                    boolean wasCreated = subfolder.mkdirs();
                    if (!wasCreated) {
                        Log.e("CapturedImages", "Failed to create directory");
                    }
                }
                File file=new File(subfolder+"/"+"image_"+secid.get(count).substring(secid.get(count++).length()-1) +".jpg");
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                decodedimage.compress(Bitmap.CompressFormat.JPEG,100,fo);
                fo.flush();
                fo.close();


            }

            //   System.out.println(imagedata);


            return secid;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return secid;
    }

}