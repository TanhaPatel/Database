package com.example.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTask extends AppCompatActivity {

    EditText addTaskEditTask;
    Button addTaskBtn;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        addTaskEditTask = findViewById(R.id.addTaskEditTask);
        addTaskBtn = findViewById(R.id.addTaskBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addtask = addTaskEditTask.getText().toString();
                if(!addtask.equals("")) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String userID = user.getUid();
                    databaseReference.child(userID).child("Task_1").setValue(addtask);
                    //databaseReference.child("Task").child(userID).child("Task_1").setValue(addtask);
                    Toast.makeText(AddTask.this, "Task " + addtask + " added successfully", Toast.LENGTH_LONG).show();
                    // reset the task to null
                    addTaskEditTask.setText("");
                }
            }
        });
    }
}
