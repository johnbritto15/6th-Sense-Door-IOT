package com.quasar.nemesis.a6thsense.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.quasar.nemesis.a6thsense.Interfaces.RecognitionInterface;
import com.quasar.nemesis.a6thsense.Models.Datum;
import com.quasar.nemesis.a6thsense.Models.Images;
import com.quasar.nemesis.a6thsense.Models.Transaction;
import com.quasar.nemesis.a6thsense.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    private static final int JOB_ID = 101;
    private JobInfo jobInfo;
    private JobScheduler jobScheduler;
    private static final int MY_PERMISSIONS_REQUEST = 123,CAMERA_PHOTO_REQUEST_CODE=1;
    private static int flag = -1;
    private String m_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant

                return;
            } else {
                flag = 1;
            }
        }

        ComponentName componentName = new ComponentName(this, MJobScheduler.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(10000);

        } else {
            builder.setPeriodic(10000);

        }
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setBackoffCriteria(3000, JobInfo.BACKOFF_POLICY_LINEAR);
        jobInfo = builder.build();
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobInfo);
        }
    }

    public void emergency(View view) {
        if (flag == 1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST);
            }
        }
    }

    public void seepastvisitors(View view) {


        // jobScheduler.cancel(JOB_ID);
        startActivity(new Intent(this, VisitorsActivity.class));
    }

    public void Process_Logout(View view) {
        jobScheduler.cancel(JOB_ID);
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    flag = 1;
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(this);
                    }
                    builder.setTitle("Warning!")
                            .setMessage("You will not be able to use the Emergency functionality!Do you want to give permissions?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE},
                                                MY_PERMISSIONS_REQUEST);
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    public void train(View view) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Enter the Persons Name");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
               File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File galleryFolder = new File(storageDirectory,"6th Sense");
                if (!galleryFolder.exists()) {
                    boolean wasCreated = galleryFolder.mkdirs();
                    if (!wasCreated) {
                        Log.e("CapturedImages", "Failed to create directory");
                    }
                }
               File subfolder=new File(storageDirectory+ "/" +"6th Sense",m_Text);
                // System.out.println("Subfolder:"+subfolder);
                if(!subfolder.exists())
                {
                    boolean wasCreated = subfolder.mkdirs();
                    if (!wasCreated) {
                        Log.e("CapturedImages", "Failed to create directory");
                    }
                }
                File file=new File(subfolder+"/"+"image_1.jpg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             //   Uri photoUri = Uri.fromFile(file);
               // intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(intent, CAMERA_PHOTO_REQUEST_CODE);


             //    Intent intent=new Intent(DashboardActivity.this,CameraActivity.class);
               //intent.putExtra("Subject",m_Text);
                //startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
            if(requestCode==CAMERA_PHOTO_REQUEST_CODE && resultCode== Activity.RESULT_OK)
            {
                Bitmap photo=(Bitmap)data.getExtras().get("data");
                System.out.println(photo.getByteCount());
                File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File galleryFolder = new File(storageDirectory,"6th Sense");
                if (!galleryFolder.exists()) {
                    boolean wasCreated = galleryFolder.mkdirs();
                    if (!wasCreated) {
                        Log.e("CapturedImages", "Failed to create directory");
                    }
                }
                File subfolder=new File(storageDirectory+ "/" +"6th Sense",m_Text);
                // System.out.println("Subfolder:"+subfolder);
                if(!subfolder.exists())
                {
                    boolean wasCreated = subfolder.mkdirs();
                    if (!wasCreated) {
                        Log.e("CapturedImages", "Failed to create directory");
                    }
                }

                String fname = "Image_1.jpg";
                File file = new File (subfolder, fname);
                if (file.exists ()) file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                File f=new File(subfolder+"/"+fname);
                System.out.println("FilePath:"+f+"\n"+"Filename:"+f.getName());
              /*  Enroll enroll=new Enroll();
                enroll.subject_id=m_Text;
                enroll.gallery_name="6th Sense";
                enroll.image=byteArray;*/

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("image/*"), f);
                MultipartBody.Part image = MultipartBody.Part.createFormData("image", f.getName(), requestFile);
                RequestBody gallery = RequestBody.create(
                        MediaType.parse("text/plain"),"6th_Sense");
                System.out.println("M_text:"+m_Text);
                RequestBody subject =  RequestBody.create(
                        MediaType.parse("text/plain"),m_Text);

               RecognitionInterface recognitionInterface= ApiClient.getClient().create(RecognitionInterface.class);
                retrofit2.Call<Datum>  call=recognitionInterface.getImages(image,subject,gallery);
                call.enqueue(new Callback<Datum>() {
                    @Override
                    public void onResponse(@NonNull Call<Datum> call, @NonNull Response<Datum> response) {
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
                            //String images=response.body().getFaceId();
                            try {
                                Datum datum = response.body();
                                List<Images> list = datum.getImages();
                                Transaction transaction = list.get(0).getTransaction();
                                String subject = transaction.getSubjectId();
                                Double confidence = transaction.getConfidence();
                                System.out.println("Subject Name:" + subject + "\n" + "Confidence:" + confidence);
                                System.out.println("dfdfd");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Datum> call, @NonNull Throwable t) {
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
