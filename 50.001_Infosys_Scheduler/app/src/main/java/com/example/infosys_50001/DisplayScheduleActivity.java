package com.example.infosys_50001;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DisplayScheduleActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_activity);
        java.util.ArrayList<String> outputText;
        outputText = getIntent().getStringArrayListExtra("dispSchedArrayList");
        java.time.LocalDateTime start = java.time.LocalDateTime.parse(outputText.get(0));
        java.time.LocalDateTime end = java.time.LocalDateTime.parse(outputText.get(1));
        int MINUTES_PERIOD=30;
        java.util.ArrayList<String> recycleSched = new Schedule(outputText.get(2)).getRecycleSchedArrayList(start,end,MINUTES_PERIOD);

        ArrayList<String> mContacts = new ArrayList<>();
        ArrayList<String> mContacts1 = new ArrayList<>();
        ArrayList<String> mContacts2 = new ArrayList<>();
        int i=0;
        for (String s:recycleSched){
            switch(i){
                case 0:
                    mContacts.add(s);
                    i++;
                    break;
                case 1:
                    mContacts1.add(s);
                    i++;
                    break;
                case 2:
                    mContacts2.add(s);
                    i=0;
                    break;
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MainAdapter(mContacts, mContacts1, mContacts2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }
}