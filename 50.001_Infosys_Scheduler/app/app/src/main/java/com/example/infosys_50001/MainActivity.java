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

    // copied from sze han
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try (InputStream instream = getContentResolver().openInputStream(uri)) {
                        Schedule empty = new Schedule("20220101T000000","20221231T235959");
                        Schedule busyschedule = new Schedule(instream);
                        String text = empty.exclude(busyschedule).toString();
                        Intent intent = new Intent(getBaseContext(), TextActivity.class);
                        intent.putExtra("outputText", text);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Handle the returned Uri

                }
            });

    //Opens up with MainActivity page, does not do anything besides lead to the login and
    //signup pages

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

        // copied from sze han
        Button selectButton = findViewById(R.id.select_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Pass in the mime type you'd like to allow the user to select
                // as the input
                mGetContent.launch("text/*");
            }
        });
    }
}