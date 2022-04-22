package com.example.infosys_50001;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Button loginhome1;
    private Button  signuphome1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginhome1 = findViewById(R.id.loginhome);
        loginhome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, loginpage.class);
                startActivity(intent);
            }
        });
        signuphome1 = findViewById(R.id.signuphome);
        signuphome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, signup.class);
                startActivity(intent);
            }
        });
    }
}