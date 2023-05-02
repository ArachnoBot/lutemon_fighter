package com.example.lutemonfighter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lutemonfighter.fragments.StartScreenFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String startScreenFragTag = "startScreenFragTag";
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new StartScreenFragment(), startScreenFragTag).commit();
    }
}