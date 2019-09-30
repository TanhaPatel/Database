package com.example.testapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText signupnameEditText;
    private EditText signupusernameEditText;
    private EditText signupemailEditText;
    private EditText signuppasswordEditText;
    private Button buttonSignup;
    private CheckBox signupshowpassword;

    //defining progressbar object
    private ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    //defining database objects
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();
            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //initializing views
        signupnameEditText = findViewById(R.id.signupnameEditTxt);
        signupusernameEditText = findViewById(R.id.signupusernameEditTxt);
        signupemailEditText = findViewById(R.id.signupemailEditTxt);
        signuppasswordEditText = findViewById(R.id.signuppasswordEditTxt);
        buttonSignup = findViewById(R.id.signupbtn1);
        signupshowpassword = findViewById(R.id.signupshowpassword);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        signupshowpassword.setOnClickListener(this);

        // database activity starts

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        // database activity ends
    }

    private void registerUser(){

        //getting email and password from edit texts
        final String email = signupemailEditText.getText().toString().trim();
        final String password  = signuppasswordEditText.getText().toString().trim();

        //getting details
        final String fullname = signupnameEditText.getText().toString().trim();
        final String username = signupusernameEditText.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length() < 6 || password.length() > 10){
            signuppasswordEditText.setError("Password should be between 6 to 10 characters");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            sendVerificationEmail();

                        } else {
                            // if failed
                            Toast.makeText(Signup.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    // show password code starts

    private void showpassword() {
        if(signupshowpassword.isChecked()) {
            // show password
            signuppasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // hide password
            signuppasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    // show password code ends

    // email verification code starts

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // email sent
                        // after email is sent just logout the user and finish this activity
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Signup.this, Login.class));
                        finish();
                    } else {
                        // email not sent, so display message and restart the activity or do whatever you wish to do
                        //restart this activity
                        overridePendingTransition(0, 0);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                    }
                }
            });
    }

    // email verification code ends

    @Override
    public void onClick(View v) {

        if(v == buttonSignup){
            registerUser();
        }

        if (v == signupshowpassword) {
            showpassword();
        }
    }
}