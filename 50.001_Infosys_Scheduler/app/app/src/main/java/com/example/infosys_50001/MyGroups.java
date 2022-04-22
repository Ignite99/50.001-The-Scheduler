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

// myGroups is the activity where sze han's code is to be placed at, it is where upload of ics and parser
// is supposed to be, has not been implemented yet since trial tests with the login side of firebase did not work.

public class MyGroups extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
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