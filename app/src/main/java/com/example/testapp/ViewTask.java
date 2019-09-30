package com.example.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTask extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    TaskRetrieve userTasks;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        userTasks = new TaskRetrieve();

        listView = findViewById(R.id.agenda);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, R.layout.view_task_info, R.id.viewtaskinfo, list);

        /*//data using username
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userid = user.getUid();
        Query specific_user = databaseReference.child(user.getEmail().substring(0,user.getEmail().indexOf("@")));*/

        //data using userid
        Query specific_user = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        specific_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    userTasks = ds.getValue(TaskRetrieve.class);
                    list.add(userTasks.getTask());

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Toast.makeText(ViewTask.this, "Failed to retrive data", Toast.LENGTH_LONG).show();
            }
        });
    }
}