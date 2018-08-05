package com.quasar.nemesis.a6thsense.Activities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.quasar.nemesis.a6thsense.Interfaces.RecognitionInterface;
import com.quasar.nemesis.a6thsense.RecognitionModels.Candidate;
import com.quasar.nemesis.a6thsense.RecognitionModels.Image;
import com.quasar.nemesis.a6thsense.RecognitionModels.RootData;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vipla on 02-03-2018.
 */

public class MJobScheduler extends JobService {
    private MJobExecutor mJobExecutor;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {

        mJobExecutor = new MJobExecutor() {
            @Override
            protected void onPostExecute(ArrayList secid) {
              //  Toast.makeText(getApplicationContext(), "executed", Toast.LENGTH_LONG).show();
                System.out.println("Executed");
                sp=getSharedPreferences("sec_id",MODE_PRIVATE);
                editor=sp.edit();
                System.out.println(secid.size());
                int count=0;
                for(int i=0;i<secid.size();i++)
                {
                    final String s=secid.get(i).toString();
                    final String key=s.substring(s.length()-1);

                    if(sp.getInt(key,0)!=Integer.parseInt(s))
                    {
                        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                       // File galleryFolder = new File(storageDirectory,"6th Sense");
                        File subfolder=new File(storageDirectory+ "/" +"6th Sense","Images");
                        File file=new File(subfolder+"/"+"image_"+key+".jpg");
                        final Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

                        editor.putInt(key,Integer.parseInt(s));
                        editor.apply();
                        System.out.println("Count "+count++);


                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                        RequestBody gallery = RequestBody.create(
                                MediaType.parse("text/plain"),"6th_Sense");

                        RecognitionInterface recognitionInterface= ApiClient.getClient().create(RecognitionInterface.class);
                        retrofit2.Call<RootData>  call=recognitionInterface.getImages(image,gallery);
                        final int finalCount = count;
                        call.enqueue(new Callback<RootData>() {
                            @Override
                            public void onResponse(@NonNull Call<RootData> call, @NonNull Response<RootData> response) {
                                System.out.println(response.isSuccessful());
                               // System.out.println(response.code());
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
                                        if(datum.getImages()!=null) {
                                            List<Image> list = datum.getImages();
                                            List<Candidate> candidate = list.get(0).getCandidates();
                                            String subject = candidate.get(0).getSubjectId();
                                            Double confidence = candidate.get(0).getConfidence();

                                            System.out.println("Subject:" + subject + "\n" + "Confidence:" + confidence);
                                        }
                                        else
                                        {
                                            System.out.println("Error");
                                            Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                                            intent.putExtra("sec_id",key);
                                            //intent.setFlags(Intent.FL | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            intent.setAction(Long.toString(System.currentTimeMillis()));
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                                                    .setContentTitle("6thSense")
                                                    .setContentText("You have a visitor!")
                                                    .setAutoCancel(true)
                                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bmp))
                                                    .setContentIntent(pendingIntent)
                                                    .build();
                                            NotificationManager mNotificationManager =
                                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            if (mNotificationManager != null) {
                                                mNotificationManager.notify(finalCount,notification);
                                            }
                                        }
                                        // System.out.println("dfdfd");
                                       // recog.setText("Person Recognized as " + subject + "\n" + "Confidence:" + confidence * 100);
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    scheduleRefresh();
                }
                jobFinished(params, false);
            }
        };
        mJobExecutor.execute(this);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobExecutor.cancel(true);
        return false;
    }

    private void scheduleRefresh() {
        JobScheduler mJobScheduler = (JobScheduler) getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder mJobBuilder =
                new JobInfo.Builder(101,
                        new ComponentName(getPackageName(),
                                MJobScheduler.class.getName()));

  /* For Android N and Upper Versions */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mJobBuilder
                    .setMinimumLatency(10 * 1000) //YOUR_TIME_INTERVAL
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setBackoffCriteria(3000,JobInfo.BACKOFF_POLICY_LINEAR)
                    .setPersisted(true)
                    .build();
            mJobScheduler.schedule(mJobBuilder.build());
        }
        else
        {
            mJobBuilder
                    .setPeriodic(10000) //YOUR_TIME_INTERVAL
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
            mJobScheduler.schedule(mJobBuilder.build());
        }
    }
}
