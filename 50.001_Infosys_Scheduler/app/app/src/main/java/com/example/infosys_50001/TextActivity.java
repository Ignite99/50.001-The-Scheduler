package com.example.infosys_50001;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// copied from sze han
public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        String outputText;
        outputText = getIntent().getStringExtra("outputText");
        TextView textView = findViewById(R.id.outputTextView);
        textView.setText(outputText);
    }
}