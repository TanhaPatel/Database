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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTask extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    UserTasks userTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        listView = findViewById(R.id.agenda);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");
       // FirebaseUser user = firebaseAuth.getCurrentUser();
        userTasks=new UserTasks();
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,R.layout.view_task_info,R.id.viewTask,list);

        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot ds:dataSnapshot.getChildren()){

                   userTasks=ds.getValue(UserTasks.class);
                   list.add(userTasks.getTask_1());


               }
               listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ViewTask.this, "Failed to retrive data", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private void showdata(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
          *//*  userTask.setUserName(ds.child(userID).getValue(UserTasks.class).getUserName());
            userTask.setEmail(ds.child(userID).getValue(UserTasks.class).getEmail());
*//*
            userTasks=ds.getValue(UserTasks.class);
            list.add(userTasks.getTask_1());
        }
        listView.setAdapter(adapter);
    }*/
}
