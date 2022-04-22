package com.example.infosys_50001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class signup extends AppCompatActivity {
    private Button sign_up;
    private Button login_return;
    private EditText emailInp, passwordInp;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailInp = findViewById(R.id.email_input);
        passwordInp = findViewById(R.id.password_input);
        sign_up = findViewById(R.id.signUp);
        progressbar = findViewById(R.id.progressbar);

        // Set on Click Listener on Registration button
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });


        //button to go back to login page, can be placed in onClick in register button as well.
        login_return = findViewById(R.id.loginsign);
        login_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, loginpage.class);
                startActivity(intent);
            }
        });
    }

    private void registerNewUser() {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailInp.getText().toString().trim();
        password = passwordInp.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //toast is feedback operation to indicate if task fails or passes
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> userEmail = new HashMap<>();
                        userEmail.put("schedule", "");
                        db.collection("User Schedule List").document(email).set(userEmail);
                        //Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());

                        Toast.makeText(getApplicationContext(),
                                "Registration successful!",
                                Toast.LENGTH_LONG)
                                .show();

                        // hide the progress bar
                        progressbar.setVisibility(View.GONE);

                    }
                    else {
                        // Registration failed
                        Toast.makeText(
                                getApplicationContext(),
                                "Registration failed!!"
                                        + " Please try again later",
                                Toast.LENGTH_LONG)
                                .show();

                        // hide the progress bar
                        progressbar.setVisibility(View.GONE);
                    }
                }
            });

    }
}