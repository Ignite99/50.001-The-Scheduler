package com.example.infosys_50001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {
    private Button createNewGroup;
    private Button JoinExistingGroup;
    private Button myGroups;

    //Leads to create group, join existing group or my groups activities.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        createNewGroup = findViewById(R.id.createGroup);
        createNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CreateNewGroup.class);
                startActivity(intent);
            }
        });
        JoinExistingGroup = findViewById(R.id.JoinExistingGroup);
        JoinExistingGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, JoinExistingGroup.class);
                startActivity(intent);
            }
        });
        myGroups = findViewById(R.id.MyGroups);
        myGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MyGroups.class);
                startActivity(intent);
            }
        });
    }
}