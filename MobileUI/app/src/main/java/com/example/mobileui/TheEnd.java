package com.example.mobileui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


public class TheEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_end);

    }

}