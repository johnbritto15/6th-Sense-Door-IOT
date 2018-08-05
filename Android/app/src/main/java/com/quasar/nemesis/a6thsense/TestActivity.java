package com.quasar.nemesis.a6thsense;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;

public class TestActivity extends AppCompatActivity {
    private AsymmetricGridView listView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }
}
