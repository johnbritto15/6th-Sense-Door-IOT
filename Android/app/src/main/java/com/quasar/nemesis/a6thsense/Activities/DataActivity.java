package com.quasar.nemesis.a6thsense.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.quasar.nemesis.a6thsense.Interfaces.RecognitionInterface;
import com.quasar.nemesis.a6thsense.R;
import com.quasar.nemesis.a6thsense.RecognitionModels.Candidate;
import com.quasar.nemesis.a6thsense.RecognitionModels.Image;
import com.quasar.nemesis.a6thsense.RecognitionModels.RootData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DataActivity extends AppCompatActivity {
    private ImageView imageView;
    private String URL="https://deep-thought-194109.appspot.com/";
    private Bitmap decodedimage;
    private int SEC_ID;
    private TextView date,time,recog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        SEC_ID=getIntent().getIntExtra("sec_id",0);
        System.out.println("SEC_ID"+" "+SEC_ID);

        new Asynch().execute();
    }

private class Asynch extends AsyncTask<Void,Void,Bitmap> {

        @Override
        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(DataActivity.this);
            progressDialog.setMessage("Fetching data...");
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Please wait");
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(URL).maxBodySize(0).timeout(0).get();

            Elements divs = doc.getElementsByTag("div");
               for(Element div:divs) {
                   Elements s = div.select("input");
                   if (Integer.parseInt(s.attr("value")) == SEC_ID) {

                       Elements p = div.select("p");
                        preferences=getSharedPreferences("timestamp",MODE_PRIVATE);
                        editor=preferences.edit();

                        editor.putString("Timestamp",p.text());
                        editor.apply();

                       Elements imagetag=div.select("img");
                       String imagedata = imagetag.attr("src");
                       imagedata = imagedata.substring(imagedata.indexOf(",") + 1);
                    //   System.out.println(imagedata);
                       byte[] decode = Base64.decode(imagedata.getBytes(), Base64.DEFAULT);
                       decodedimage = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                    //   System.out.println(decodedimage.getByteCount());
                       return decodedimage;
                   }
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
return null;
        }
        @Override
        protected void onPostExecute(Bitmap decodedimage)
        {
            imageView=(ImageView)findViewById(R.id.photo);
            imageView.setImageBitmap(decodedimage);

            date=(TextView)findViewById(R.id.date);
            String timest=preferences.getString("Timestamp","0");
            String[] stamp;
            stamp=timest.split(" ");
            date.setText(String.format("%s%s", date.getText(), stamp[0]));
            time=(TextView)findViewById(R.id.time);
            time.setText(String.format("Time:%s", stamp[1]));
            if(progressDialog.isShowing())
                progressDialog.dismiss();


            File f = new File(getBaseContext().getCacheDir(), "image");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            decodedimage.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);

                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }

            recog=(TextView)findViewById(R.id.recognize);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/*"), f);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", f.getName(), requestFile);
            RequestBody gallery = RequestBody.create(
                    MediaType.parse("text/plain"),"6th_Sense");

            RecognitionInterface recognitionInterface= ApiClient.getClient().create(RecognitionInterface.class);
            retrofit2.Call<RootData>  call=recognitionInterface.getImages(image,gallery);
            call.enqueue(new Callback<RootData>() {
                @Override
                public void onResponse(Call<RootData> call, Response<RootData> response) {
                    System.out.println(response.isSuccessful());
                    System.out.println(response.code());
                    Log.d("DashboardActivity","onResponse:"+response.code());
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override public void log(String message) {
                            Log.d("Success:", message);
                        }
                    });
                    if(response.isSuccessful())
                    {
                      //  String images=response.body().toString();
                        try {
                            RootData datum = response.body();
                            List<Image> list = datum.getImages();
                            List<Candidate> candidate = list.get(0).getCandidates();
                            String subject = candidate.get(0).getSubjectId();
                            Double confidence = candidate.get(0).getConfidence();

                            System.out.println("Subject:" + subject + "\n" + "Confidence:" + confidence);
                            // System.out.println("dfdfd");
                            recog.setText("Person Recognized as " + subject + "\n" + "Confidence:" + confidence * 100);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RootData> call, Throwable t) {
                    System.out.println("No luck");
                    System.out.println(t.toString());
                    System.out.println(Arrays.toString(t.getStackTrace()));
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override public void log(String message) {
                            Log.d("Message", message);
                        }
                    });


                }
            });

        }


        }
    }


