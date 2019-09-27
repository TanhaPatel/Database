package com.example.testapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //defining views
    private EditText emailEditTxt;
    private EditText passwordEditTxt;
    private Button loginbtn;
    private Button signupbtn;
    private CheckBox loginshowpassword;

    //google signin btn obj
    private SignInButton gsignin;

    //progress dialog obj
    private ProgressDialog progressDialog;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private final static int RC_SIGN_IN = 2;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //initializing views
        emailEditTxt = findViewById(R.id.emailEditTxt);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);
        loginbtn = findViewById(R.id.loginbtn);
        signupbtn = findViewById(R.id.signupbtn);
        gsignin = findViewById(R.id.gsignin);
        loginshowpassword = findViewById(R.id.loginshowpassword);

        progressDialog = new ProgressDialog(this);

        // Build a GoogleSignInClient with the options specified by gso.
        loginbtn.setOnClickListener(this);
        signupbtn.setOnClickListener(this);
        gsignin.setOnClickListener(this);
        loginshowpassword.setOnClickListener(this);
    }

    //method for user login starts

    private void userLogin() {
        String email = emailEditTxt.getText().toString().trim();
        String password = passwordEditTxt.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length() < 6 || password.length() > 10){
            passwordEditTxt.setError("Password should be between 6 to 10 characters");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }  else {
                            Toast.makeText(Login.this, "You don't have registered. Please register first.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this, Signup.class));
                        }
                    }
                });
    }

    //method for user login ends

    // show password code starts

    private void showpassword() {
        if(loginshowpassword.isChecked()) {
            // show password
            passwordEditTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // hide password
            passwordEditTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    // show password code ends

    //goes to signup page
    private void signUp() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        if (v == loginbtn) {
            userLogin();
        }

        if (v == signupbtn) {
            finish();
            startActivity(new Intent(this, Signup.class));
        }

        if (v == gsignin) {
            signUp();
        }

        if (v == loginshowpassword) {
            showpassword();
        }
    }


    // Configure Google Sign In
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                progressDialog.setMessage("Signing In");
                progressDialog.show();
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Failed Sign In", Toast.LENGTH_SHORT);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(Login.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                            // startActivity(new Intent(Login.this,AfterLogin1.class));
                            // finish();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this,"Failed to Sign in",Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if (user != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}