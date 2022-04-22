package com.example.infosys_50001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class JoinExistingGroup extends AppCompatActivity {
    // Join an existing group
    TextView explanation;
    EditText input_test;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_existing_group);

        //This is just a short test to replace textview with the input text.

        explanation = findViewById(R.id.Explanation_group_code);
        input_test = findViewById(R.id.newGroupCode);
        submit = findViewById(R.id.submit_group_code);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("Group List").document(input_test.getText().toString().trim());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();  // if the doc exist, user joins the group
                            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            if (document.exists()) {
                                Map<String, Object> GroupData = document.getData();
                                int n = Integer.valueOf(GroupData.get("Number of Members").toString());
                                boolean newToGrp = true;
                                for (Map.Entry<String,Object> entry: GroupData.entrySet()){
                                    if (entry.getValue().equals(userEmail)){
                                        Toast.makeText(JoinExistingGroup.this, "You are already in the group!", Toast.LENGTH_SHORT).show();
                                        newToGrp = false;
                                    }
                                }
                                if (newToGrp) {
                                    n++;
                                    GroupData.put(Integer.toString(n), userEmail);
                                    GroupData.put("Number of Members", Integer.toString(n));
                                    docRef.set(GroupData);
                                    Toast.makeText(JoinExistingGroup.this, "You have successfully joined the group!", Toast.LENGTH_SHORT).show();

                                    // add group to the userEmail in User Schedule List
                                    DocumentReference UserRef = db.collection("User Schedule List").document(userEmail);
                                    UserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();  // if the doc exist, user joins the group
                                                Map<String, Object> UserData = document.getData();
                                                int n = Integer.valueOf(UserData.get("User Group Number").toString());
                                                n += 1;
                                                UserData.put("User Group Number", Integer.toString(n));
                                                UserData.put(Integer.toString(n), input_test.getText().toString());
                                                UserRef.set(UserData);
                                            }
                                        }
                                    });
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "This group doesn't exists!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });


    }
}