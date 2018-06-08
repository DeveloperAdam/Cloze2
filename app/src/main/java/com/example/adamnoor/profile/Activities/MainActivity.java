package com.example.adamnoor.profile.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.adamnoor.profile.Utils.sharePrefrence;
import com.techease.profile.R;

public class MainActivity extends AppCompatActivity {

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
