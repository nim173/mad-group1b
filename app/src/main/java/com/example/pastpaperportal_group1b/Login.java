package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity {

    private static final String TAG = "FacebookLogin";
    private static final  int RC_SIGN_IN=100;
    GoogleSignInClient mGoogleSignInClient;

    EditText Email;
    EditText Password;
    Button login,signUp;
    SignInButton mgoogleButton;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;

    private CallbackManager mCallbackManager;


    //progerss dialog
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);




       // TextView toSignUp= findViewById(R.id.txtSignUp);
        TextView tochangepsw= findViewById(R.id.txtforgetpws);
        Email=findViewById(R.id.loginEmail);
        Password=findViewById(R.id.loginPassword);
        login=findViewById(R.id.btnLogin);
        signUp=findViewById(R.id.toSign);

        mgoogleButton=findViewById(R.id.googleSign);



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth= FirebaseAuth.getInstance();

        //action bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Login");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init progress dialog

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loggin In");


        //login button click

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=Email.getText().toString();
                String pass=Password.getText().toString().trim();

                //validation


                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    Email.setError("Invalid Email");
                    Email.setFocusable(true);

                }
                else if(pass.length()<6){

                    Password.setError("Password length at least 6 characters");
                    Password.setFocusable(true);

                }
                else if(email.equals("admin@gmail.com")&& (pass.equals("admin1"))){
                    Intent intent = new Intent(Login.this,admin_dashboard.class);
                    intent.putExtra("user_name","Admin");
                    startActivity(intent);
                    Toast.makeText(Login.this, "Welcome to Admin DashBoard", Toast.LENGTH_SHORT).show();
                }

                else{
                    // valid email

                   // adminLogin(email,pass);
                    loginUser(email,pass);
                }

            }
        });


        //handle google Sign in
        mgoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent signIntent=mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signIntent,RC_SIGN_IN);

            }
        });




//click SignUP Text
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup=new Intent(Login.this,SignUp.class);
                startActivity(signup);
            }
        });

//Click forgetPasword text
        tochangepsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showRecoverPassWordDialog();
            }
        });



    }


    //forget password
    private void showRecoverPassWordDialog() {

        //Alter Dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        //set layout

        LinearLayout linearLayout=new LinearLayout(this);

        //view dialog

         final EditText etEmail=new EditText(this);
        etEmail.setHint("Email");
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        etEmail.setMinEms(16);

        linearLayout.addView(etEmail);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email=etEmail.getText().toString().trim();
                beginRecover(email);


            }
        });
        //button cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        //show dialog
        builder.create().show();



    }
//recover email
    private void beginRecover(String email) {

        progressDialog.setMessage("Sending...");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(!task.isSuccessful()){
                    Toast.makeText(Login.this, "Faield", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Login.this, "Sent... ", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loginUser(String email, String pass) {

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                       FirebaseUser user=mAuth.getCurrentUser();


                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                          //  startActivity(new Intent(Login.this,ProfileDefault.class));
                            try {

                                if(user.isEmailVerified()){
                                    Toast.makeText(Login.this, "Email Verifyed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this,SearchCommon.class));
                                }
                                else{
                                    Toast.makeText(Login.this, "Verfy Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (NullPointerException e){

                                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            //finish();


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();//go privious activity

        return super.onSupportNavigateUp();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);



    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Pass the activity result back to the Facebook SDK


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }

    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get User email and uid from auth
                           String  personname=user.getDisplayName();
                            String email=user.getEmail();
                            String uid=user.getUid();
                            String url=user.getPhotoUrl().toString();

                            //emali exsict


                            DatabaseReference db=FirebaseDatabase.getInstance().getReference("Users");
                            Query getdata=db.orderByChild("email").equalTo(email);
                            getdata.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Toast.makeText(Login.this, "Exists Email", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        HashMap<Object,String> hashMap= new HashMap<>();

                                        //put info in hashmap

                                        //hashMap.put("email",email);
                                        hashMap.put("uid",uid);
                                        hashMap.put("name",personname);
                                        hashMap.put("url",url);
                                        hashMap.put("Cover_Photo","");

                                        db.child(uid).setValue(hashMap);

                                        Toast.makeText(Login.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(Login.this, ProfileDefault.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            //using Hashmap

                            //firbase database instence

                           // FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

                            //path to store
                           // DatabaseReference reference=firebaseDatabase.getReference("Users");

                            //reference.child(uid).setValue(hashMap);




                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this,"You are not able to log in to google",Toast.LENGTH_LONG).show();
                           // updateUI(null);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
