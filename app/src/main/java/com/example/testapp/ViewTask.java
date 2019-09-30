package com.example.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

        /*// Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot ds : dataSnapshot.getChildren()){
                   userTasks = ds.getValue(TaskRetrieve.class);
                   list.add(userTasks.getTask());

               }
               listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ViewTask.this, "Failed to retrive data", Toast.LENGTH_LONG).show();
            }
        });*/

        Query specific_user = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        specific_user.addListenerForSingleValueEvent(
                new ValueEventListener() {
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


/* 1st method
private void getUserData()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");

        reference.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String familyname=datas.child("familyName").getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
*/

/* 2nd method
private void getUserData()
{
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    String userid=user.getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
    reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String email = dataSnapshot.getValue(User.class).getEmail();
            String firstName = dataSnapshot.getValue(User.class).getFirstName();
            Log.d("Datasnapshot",email+" "+firstName);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
*/

/* 3rd method
private void getUserData() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("user");

    Query specific_user = myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    specific_user.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //here you will get the data
                    String email = dataSnapshot.getValue(User.class).getEmail();
                    String firstName = dataSnapshot.getValue(User.class).getFirstName();
                    Log.d("Datasnapshot", email + " " + firstName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {



                }
            });
}
*/

/*4th method
DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");

reference.orderByChild("firstName").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
 @Override
public void onDataChange(DataSnapshot dataSnapshot) {
for(DataSnapshot datas: dataSnapshot.getChildren()){
   String familyname=datas.child("familyName").getValue().toString();
    }
 }
   @Override
public void onCancelled(DatabaseError databaseError) {
  }
 });
*/

/*5th method
Query specific_user = myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
      specific_user.addListenerForSingleValueEvent(
              new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                     //here you will get the data
                  }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                                           }
                           });
*/