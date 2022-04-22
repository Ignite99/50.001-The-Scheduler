package com.example.infosys_50001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;

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
                String inpGroupName = input_test.getText().toString().trim();

//              scan through groups in group list to check if group name already exists, before creating a new group
                CollectionReference groupRef = db.collection("Group List");
                groupRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // iterates through existing groups in Group List
                        Boolean groupDoesNotExists = true;
                        if (task.isSuccessful()) {
                            QuerySnapshot docGroup = task.getResult();
                            for (QueryDocumentSnapshot existingGroupName : docGroup) {
                                if (inpGroupName.equals(existingGroupName.getId())) {
                                    Toast.makeText(CreateNewGroup.this, "This group already exists!", Toast.LENGTH_SHORT).show();
                                    groupDoesNotExists = false;
                                }
                            }
                        }
                        if (groupDoesNotExists){
                            // create new group
                            Map<String, String> groupName = new HashMap<>();
                            groupName.put("1", FirebaseAuth.getInstance().getCurrentUser().getEmail());  // store user email to list
                            groupName.put("Number of Members", "1");
                            // add group to firebase storage
                            db.collection("Group List").document(input_test.getText().toString()).set(groupName);
                            explanation.setText("Group code: " + input_test.getText() + " has been created!");

                            // add group to UserData (keep track of what groups the user is in)
                            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
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
                });
            }
        });
    }
}