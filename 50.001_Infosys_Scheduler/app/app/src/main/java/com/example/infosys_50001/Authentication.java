package com.example.infosys_50001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class Authentication extends AppCompatActivity {

    private TextView auth;

    //check if authentication of firebase is correct or wrong.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // initialising all views through id defined above
        auth = findViewById(R.id.authentication_signal);
        auth.setText("Task has been completed!");
    }
}