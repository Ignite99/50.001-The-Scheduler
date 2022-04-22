package com.example.infosys_50001;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyGroups extends AppCompatActivity {
    // specify the stored shared prefs location
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";
    // MyGroup's key to retrieve string of groups that user is in
    public static final String KEY = "userGroups";
    SharedPreferences myGroupsPreferences;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try (InputStream instream = getContentResolver().openInputStream(uri)) {
                        java.time.LocalDateTime analysis_start_datetime = java.time.LocalDateTime.now();
                        java.time.LocalDateTime analysis_end_datetime = analysis_start_datetime.plusDays(30);
                        Schedule curr_user_busyblocks = new Schedule(instream);
                        String curr_user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> entries = new HashMap<>();
                        entries.put("schedule", curr_user_busyblocks.toIcs());
                        db.collection("User Schedule List").document(curr_user_email).update(entries)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Upload successful!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Upload Failed!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();;
                                        }
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
    public void szcallback() {
        myGroupsPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // this userGroupsStr contains a string of all the groups
        String userGroupsStr = myGroupsPreferences.getString(KEY, "");

        //String list
        String[] groupArr = userGroupsStr.split(", ");

        //unused
        //ArrayList<String> newGroupList = new ArrayList(Arrays.asList(groupList) );

        mRecyclerView = (RecyclerView) findViewById(R.id.Group_List);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        //array list here
        mAdapter = new GroupView(groupArr);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        // upload schedule
        Button uploadSchedule = findViewById(R.id.upload_schedule);
        uploadSchedule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Pass in the mime type you'd like to allow the user to select
                // as the input;
                mGetContent.launch("text/calendar");
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // from user retrieve groups that they are in, store it in shared preferences
        String userEmail = user.getEmail();
        DocumentReference UserRef = db.collection("User Schedule List").document(userEmail);
        UserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();  // if the doc exist, user joins the group
                    Map<String, Object> UserData = document.getData();

                    int n = Integer.valueOf(UserData.get("User Group Number").toString());

                    String userGroups = "";
                    ArrayList<String> sztest = new ArrayList<>();

                    if (n > 0) {
                        for (int i = 1; i <= n; i++) {
                            userGroups = userGroups + (String) UserData.get(Integer.toString(i)) + ", ";
                            sztest.add((String) UserData.get(Integer.toString(i)));
                        }

                        userGroups = userGroups.substring(0, userGroups.length() - 2); // remove last unwanted ", "

                        // use shared preferences editor to save the string of user's groups into shared preferences
                        myGroupsPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
                        SharedPreferences.Editor myGroupsPreferencesEditor = myGroupsPreferences.edit();
                        myGroupsPreferencesEditor.putString(KEY, userGroups);
                        Log.e("TAG","stringput: "+userGroups);
                        myGroupsPreferencesEditor.apply();
                        szcallback();
                    }
                    // else userGroups is ""
                }
            }

        });

//        // ------------------------------
//        // NOT supposed to be here: from selected grp, get all users from that group
//        String selectedGroup = "uwu";
//
//        DocumentReference GroupRef = db.collection("Group List").document(selectedGroup);
//        GroupRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();  // if the doc exist, user joins the group
//                    Map<String, Object> GroupData = document.getData();
//
//                    int n = Integer.valueOf(GroupData.get("Number of Members").toString());
//
//                    String groupUsers = "";
//
//                    if (n > 0) {
//                        for (int i = 1; i <= n; i++) {
//                            groupUsers = groupUsers + (String) GroupData.get(Integer.toString(i)) + ", ";
//                        }
//
//                        groupUsers = groupUsers.substring(0, groupUsers.length() - 2); // remove last unwanted ", "
//
//                        // use shared preferences editor to save the string of user's groups into shared preferences
//                        myGroupsPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//                        SharedPreferences.Editor myGroupsPreferencesEditor = myGroupsPreferences.edit();
//                        myGroupsPreferencesEditor.putString(KEY2, groupUsers);
//                        myGroupsPreferencesEditor.apply();
//                    }
//                    // else groupUsers is ""
//                }
//            }
//        });
//        // ^NOT SUPPOSED TO BE HERE
//        // ---------------------------------


    }
}