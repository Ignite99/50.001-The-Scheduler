package com.example.infosys_50001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;

//Sze Han's side of the code have not implemented, trying to find out how to push to firebase

public class CreateNewGroup extends AppCompatActivity {
    TextView explanation;
    EditText input_test;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        //This is just a short test to replace textview with the input text.

        explanation = findViewById(R.id.Explanation_group_code);
        input_test = findViewById(R.id.newGroupCode);
        submit = findViewById(R.id.submit_group_code);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> groupName = new HashMap<>();
                LinkedList<String> a = new LinkedList<String>();
                a.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                groupName.put("Group Members", a);
                db.collection("Group List").document(input_test.getText().toString()).set(groupName);
                explanation.setText("Group code: " + input_test.getText() + " has been created!");
            }
        });
    }
}