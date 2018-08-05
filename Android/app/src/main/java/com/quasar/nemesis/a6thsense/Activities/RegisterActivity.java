package com.quasar.nemesis.a6thsense.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quasar.nemesis.a6thsense.R;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register=(Button)findViewById(R.id.signup);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==register)
        { RegisterActivity.this.finish();
            startActivity(new Intent(RegisterActivity.this,DataActivity.class));

        }
    }


}
