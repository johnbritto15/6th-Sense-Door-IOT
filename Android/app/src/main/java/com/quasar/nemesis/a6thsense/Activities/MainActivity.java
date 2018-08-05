package com.quasar.nemesis.a6thsense.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.quasar.nemesis.a6thsense.MyApplication;
import com.quasar.nemesis.a6thsense.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button logout, call1, call2;
    private CircleImageView emergency1, emergency2;
    private TextView t1, t2;
    private FloatingActionButton ad, re, add, rem;
    private String number="0", number2="0";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int SELECT_PHONE_NUMBER = 0;
    private int SELECT_PHONE_NUMBER_2 = 1;

    // private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  System.out.println("activity" + " " + count);
      /*  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/
        emergency1 = (CircleImageView) findViewById(R.id.emer1);
        emergency2 = (CircleImageView) findViewById(R.id.emer2);
        ad = (FloatingActionButton) findViewById(R.id.add1);
        re = (FloatingActionButton) findViewById(R.id.rem1);
        add = (FloatingActionButton) findViewById(R.id.add2);
        rem = (FloatingActionButton) findViewById(R.id.rem2);
        ad.setOnClickListener(this);
        re.setOnClickListener(this);
        add.setOnClickListener(this);
        rem.setOnClickListener(this);
        t1 = (TextView) findViewById(R.id.name1);
        t2 = (TextView) findViewById(R.id.name2);
       // logout = (Button) findViewById(R.id.signout);
        //logout.setOnClickListener(this);
        call1 = (Button) findViewById(R.id.phone1);
        call1.setOnClickListener(this);
        call2 = (Button) findViewById(R.id.phone2);
        call2.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("Name", MODE_PRIVATE);
        String n1 = sharedPreferences.getString("1", "0");
        String n2 = sharedPreferences.getString("2", "0");
        if (!n1.equals("0")) {
            t1.setText(n1);
        }
        if (!n2.equals("0")) {
            t2.setText(n2);
        }
        sharedPreferences = getSharedPreferences("Images", MODE_PRIVATE);
        n1 = sharedPreferences.getString("1", "0");
        n2 = sharedPreferences.getString("2", "0");
        if (!n1.equals("0")) {
           Picasso.with(this).load(Uri.parse(n1)).into(emergency1);
        }
        if (!n2.equals("0")) {
            Picasso.with(this).load(Uri.parse(n2)).into(emergency2);
        }

        sharedPreferences = getSharedPreferences("Number", MODE_PRIVATE);
        n1 = sharedPreferences.getString("1", "0");
        n2 = sharedPreferences.getString("2", "0");
        if (!n1.equals("0")) {
            number=n1;
        }
        if (!n2.equals("0")) {
           number2=n2;
        }


    }

    @Override
    public void onClick(View v) {
        if (v == ad) {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, SELECT_PHONE_NUMBER);
        } else if (v == add) {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, SELECT_PHONE_NUMBER_2);
        }else if(v== re)
        {
            t1.setText("");
            emergency1.setImageResource(R.drawable.ic_account);
            number="0";
            sharedPreferences = getSharedPreferences("Name", MODE_PRIVATE);
            sharedPreferences.edit().remove("1").apply();



            sharedPreferences = getSharedPreferences("Images", MODE_PRIVATE);
            sharedPreferences.edit().remove("1").apply();




            sharedPreferences = getSharedPreferences("Number", MODE_PRIVATE);
            sharedPreferences.edit().remove("1").apply();



        }
        else if(v== rem)
        {
            t2.setText("");
            emergency2.setImageResource(R.drawable.ic_account);
            number2="0";
            sharedPreferences = getSharedPreferences("Name", MODE_PRIVATE);
            sharedPreferences.edit().remove("2").apply();



            sharedPreferences = getSharedPreferences("Images", MODE_PRIVATE);
            sharedPreferences.edit().remove("2").apply();




            sharedPreferences = getSharedPreferences("Number", MODE_PRIVATE);
            sharedPreferences.edit().remove("2").apply();

        }
        else if (v == call1) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if(number.equals("0"))
            {
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setMessage("Please set an Emergency Contact")
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    dialog.cancel();
                                    }}).show();
                return;
            }
            callIntent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        }
        else if(v==call2)
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if(number2.equals("0"))
            {
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setMessage("Please set an Emergency Contact")
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.cancel();
                            }}).show();
                return;
            }
            callIntent.setData(Uri.parse("tel:"+number2));
            startActivity(callIntent);
        }
        else if(v==logout)
        {
            ((MyApplication)this.getApplication()).getclient().signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                            MainActivity.this.finish();
                           Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);

                        }
                    });

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (requestCode == SELECT_PHONE_NUMBER && resultCode == RESULT_OK  && SELECT_PHONE_NUMBER==0) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] names=new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            if(contactUri!=null) {
                Cursor cursor = this.getContentResolver().query(contactUri, projection,
                        null, null, null);
                Cursor cursor2 = this.getContentResolver().query(contactUri, names,
                        null, null, null);

                // If the cursor returned is valid, get the phone number
                if ((cursor != null && cursor.moveToFirst()) && (cursor2 != null && cursor2.moveToFirst())) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int index = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    number = cursor.getString(numberIndex);
                    String name = cursor2.getString(index);
                    //emergency1.setText(name);
                    System.out.println(number + "  " + name);
                    // Do something with the phone number
                    t1.setText(name);

                    Uri photo = retrieveContactPhoto(this, number);
                    emergency1.setImageURI(photo);

                    sharedPreferences=getSharedPreferences("Number",MODE_PRIVATE);
                    editor=sharedPreferences.edit();
                    editor.putString("1",number);
                    editor.apply();

                    sharedPreferences=getSharedPreferences("Images",MODE_PRIVATE);
                    editor=sharedPreferences.edit();
                    editor.putString("1",photo.toString());
                    editor.apply();

                    sharedPreferences=getSharedPreferences("Name",MODE_PRIVATE);
                    editor=sharedPreferences.edit();
                    editor.putString("1",name);
                    editor.apply();

                    Picasso.with(this).load(photo).into(emergency1);
                    if (emergency1.getDrawable() == null)
                        emergency1.setImageResource(R.drawable.ic_account);
                    //emergency1.setImageURI(photo);

                }
                if (cursor != null) {
                    cursor.close();
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
            }
        }
        else if (requestCode == SELECT_PHONE_NUMBER_2 && resultCode == RESULT_OK  && SELECT_PHONE_NUMBER_2==1) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] names = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            if (contactUri != null) {
                Cursor cursor = this.getContentResolver().query(contactUri, projection,
                        null, null, null);
                Cursor cursor2 = this.getContentResolver().query(contactUri, names,
                        null, null, null);

                // If the cursor returned is valid, get the phone number
                if ((cursor != null && cursor.moveToFirst()) && (cursor2 != null && cursor2.moveToFirst())) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int index = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    number2 = cursor.getString(numberIndex);
                    String name = cursor2.getString(index);
                    //emergency2.setText(name);
                    System.out.println(number2 + "  " + name);
                    t2.setText(name);
                    // Do something with the phone number
                    Uri photo = retrieveContactPhoto(this, number2);
                    emergency2.setImageURI(photo);

                    sharedPreferences=getSharedPreferences("Number",MODE_PRIVATE);
                    editor=sharedPreferences.edit();
                    editor.putString("2",number2);
                    editor.apply();

                    sharedPreferences=getSharedPreferences("Images",MODE_PRIVATE);
                    editor=sharedPreferences.edit();
                    editor.putString("2",photo.toString());
                    editor.apply();

                    sharedPreferences=getSharedPreferences("Name",MODE_PRIVATE);
                    editor=sharedPreferences.edit();
                    editor.putString("2",name);
                    editor.apply();
                    Picasso.with(this).load(photo).into(emergency2);
                    if (emergency2.getDrawable() == null)
                        emergency2.setImageResource(R.drawable.ic_account);

                }

                if (cursor != null) {
                    cursor.close();
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
            }
        }
    }



    public static Uri retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            System.out.println(contactId);
            cursor.close();
        }
        Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
        System.out.println(my_contact_Uri);
        return my_contact_Uri;
    }

}
