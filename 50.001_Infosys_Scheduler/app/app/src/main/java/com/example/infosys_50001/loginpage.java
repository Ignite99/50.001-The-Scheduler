package com.example.infosys_50001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class loginpage extends AppCompatActivity {
    private Button cheats;
    private EditText emailLogin, passwordLogin;
    private Button login;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;

    // specify the stored shared prefs location
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";
    // loginpage's key to retrieve previous login name from shared preferences
    public static final String KEY = "personName";
    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // Creation of cache for email
        // get shared preferences
        loginPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        // if there was a cached name, retrieve it using KEY, else return ""
        String cachedName = loginPreferences.getString(KEY, "");
        // set the cached name into editTextTextPersonName
        EditText personNameEditText = findViewById(R.id.login_email);
        personNameEditText.setText(cachedName);


        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // initialising all views through id defined above
        emailLogin = findViewById(R.id.login_email);
        passwordLogin = findViewById(R.id.login_password);
        login = findViewById(R.id.loginlog);
        progressbar = findViewById(R.id.progressBar);

        // Set on Click Listener on Sign-in button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });


        // A button to get into home page of app without loggin in. MUST REMOVE WHEN FIREBASE LOGIN
        // WORKS.
        cheats = findViewById(R.id.cheating);
        cheats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the input name from editTextTextPersonName
                EditText personNameEditText = findViewById(R.id.login_email);
                String inputName = personNameEditText.getText().toString();
                // use shared preferences editor to save the input name into shared preferences
                SharedPreferences.Editor loginPreferencesEditor = loginPreferences.edit();
                loginPreferencesEditor.putString(KEY, inputName);
                loginPreferencesEditor.apply();

                Intent intent = new Intent(loginpage.this, HomePage.class);
                startActivity(intent);
            }
        });
    }


    private void loginUserAccount()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailLogin.getText().toString().trim();
        password = passwordLogin.getText().toString();

        // validations for input email and password
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

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(
                            @NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            //Toast is a feedback operation to show if operation is a success or fail
                            Toast.makeText(getApplicationContext(),
                                    "Login successful!!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);

                            // if sign-in is successful
                            // got to home page
                            Intent intent = new Intent(loginpage.this, HomePage.class);
                            startActivity(intent);
                        }

                        else {
                            // sign-in failed
                            Toast.makeText(getApplicationContext(),
                                    "Login failed!!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}