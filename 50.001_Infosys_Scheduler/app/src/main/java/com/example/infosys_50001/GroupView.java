package com.example.infosys_50001;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class GroupView extends RecyclerView.Adapter<GroupView.ViewHolder> {

    // change this to string array for group name
    String[] Group_name;

    public GroupView(String[] ls_of_group_names) {
        Group_name = ls_of_group_names;
    }

    @NonNull
    @Override
    public GroupView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupview_template, parent, false);
        return new GroupView.ViewHolder(view);
    }

    public void cusOnClick(View view,String[] icslist,int days){
        java.time.LocalDateTime analysis_start_datetime = java.time.LocalDateTime.now();
        java.time.LocalDateTime analysis_end_datetime = analysis_start_datetime.plusDays(days);
        Schedule freesched = new Schedule(analysis_start_datetime,analysis_end_datetime);
        java.util.ArrayList<Schedule> arraylist_of_busy_schedules = new java.util.ArrayList<Schedule>();

        //ALL BUSY SCHEDS OF ALL GROUP MEMBERS GETS PLACED INTO arraylist_of_busy_schedules
        for (String ics:icslist){
            Schedule busyschedule = new Schedule(ics);
            arraylist_of_busy_schedules.add(busyschedule);
        }
        for (Schedule busysched:arraylist_of_busy_schedules) {
            freesched = freesched.exclude(busysched);
        }
        Intent intent = new Intent(view.getContext(), DisplayScheduleActivity.class);
        java.util.ArrayList<String> to_send = new ArrayList<>();
        to_send.add(analysis_start_datetime.toString());
        to_send.add(analysis_end_datetime.toString());
        to_send.add(freesched.toIcs());
        intent.putExtra("dispSchedArrayList", to_send );
        view.getContext().startActivity(intent);
    }
    @Override
    public void onBindViewHolder(@NonNull GroupView.ViewHolder holder, int position) {
        //unused
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // ------------------------------
        //from selected grp, get all users from that group
//        for (String i:Group_name){
//            Log.e("TAG","item: "+i);
//        }
        DocumentReference GroupRef = db.collection("Group List").document(Group_name[position]);
        GroupRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();  // if the doc exist, user joins the group
                    Map<String, Object> GroupData = document.getData();

                    int n = Integer.valueOf(GroupData.get("Number of Members").toString());

                    StringBuilder membersString = new StringBuilder();

                    if (n > 0) {
                        for (int i = 1; i <= n; i++) {
                            membersString.append(((String) GroupData.get(Integer.toString(i))).trim());
                            membersString.append("\n");
                        }

                        holder.mMembers.setText(membersString.toString());
                    }
                    else {
                        holder.mMembers.setText("");
                    }
                    // get all the schedules and put it into holder.mUserSchedules
                    String[] usersArr = holder.mMembers.getText().toString().split("\n");
                    for (String iterateUser : usersArr) {
                        DocumentReference tempRef = db.collection("User Schedule List").document(iterateUser);
                        tempRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();  // if the doc exist, user joins the group
                                    Map<String, Object> UserData = document.getData();

                                    String schedule = (String) UserData.get("schedule");

                                    String temp = holder.mUsersSchedule.getText().toString();
                                    String appendedSchedule = temp + schedule + "!SEPARATOR!";
                                    holder.mUsersSchedule.setText(appendedSchedule);
                                }
                            }
                        });
                    }
                }
            }
        });

        DocumentReference UserRef = db.collection("Group List").document(Group_name[position]);
        UserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();  // if the doc exist, user joins the group

                    holder.mGroupName.setText(Group_name[position]);
                    // "holder.mMembers.setText" has been moved to line 96
                    holder.mSevenDay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // String array containing all the users' schedules
                            String[] usersSchedulesArr = holder.mUsersSchedule.getText().toString().split("!SEPARATOR!");

                            cusOnClick(view, usersSchedulesArr,7);
                        }
                    });
                    holder.mThirtyDay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String[] usersSchedulesArr = holder.mUsersSchedule.getText().toString().split("!SEPARATOR!");
                            cusOnClick(view,usersSchedulesArr,30);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Group_name.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mGroupName;
        public TextView mMembers;
        public TextView mUsersSchedule;
        public Button mSevenDay;
        public Button mThirtyDay;

        public ViewHolder(View itemView) {
            super(itemView);
            mGroupName = itemView.findViewById(R.id.grup_name);
            mMembers = itemView.findViewById(R.id.users_in_group);
            mSevenDay = itemView.findViewById(R.id.seven_day);
            mThirtyDay = itemView.findViewById(R.id.thirty_day);
            mUsersSchedule = itemView.findViewById(R.id.usersSchedule);
        }
    }
}
