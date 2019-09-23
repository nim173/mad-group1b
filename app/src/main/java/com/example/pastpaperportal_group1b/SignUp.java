package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {

    EditText  pass,Email,name;
    Button   signup,signIN;
//     FirebaseAuth.AuthStateListener mAuthListener;
     FirebaseAuth mAuth;
     ProgressDialog progressDialog;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_sign_up);

        Email=findViewById(R.id.regiEmail);
        pass=findViewById(R.id.regiPassword);


        //name=findViewById(R.id.regiUserName);
        mAuth= FirebaseAuth.getInstance();
        signup=findViewById(R.id.btnSignUp);
        signIN=findViewById(R.id.stoLogin);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering com.example.pastpaperportal_group1b.User..");

        //handle register button click

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String password = pass.getText().toString().trim();
                //  String uName=name.getText().toString().trim();

                //validation

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Email.setError("Invalid Email");
                    Email.setFocusable(true);

                } else if (password.length() < 6) {

                    pass.setError("Password length at least 6 characters");
                    pass.setFocusable(true);

                }


                else {
                    registorUser(email, password);
                }

            }



        });


        //progress bar
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Registaring");


        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);







        //click Sign Text
       // TextView mtxtSignIn =  findViewById(R.id.txtSignIn);
        signIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent login=new Intent(SignUp.this, Login.class);
              startActivity(login);

            }
        });


    }

    private void registorUser(String email, String password) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information



                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            sendEmailVerfication();



                            //get User email and uid from auth
                            String personName=user.getDisplayName();
                            String email=user.getEmail();
                            String uid=user.getUid();
                          //  user.getPhotoUrl().toString();

                            //using Hashmap
                            HashMap<Object,String> hashMap= new HashMap<>();

                            //put info in hashmap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("url","");
                            hashMap.put("cover","");



                            //firbase database instence

                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

                            //path to store
                            DatabaseReference reference=firebaseDatabase.getReference("Users");

                            reference.child(uid).setValue(hashMap);


                            Toast.makeText(SignUp.this, "Registed..\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this,Login.class));
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.

                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
           
                progressDialog.dismiss();
                Toast.makeText(SignUp.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void sendEmailVerfication(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        mAuth.signOut();

                        Toast.makeText(SignUp.this, "Please Check Your Email And Verify", Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(SignUp.this, "Not Verify", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }


    public void toComments_Images(View view){
        Intent intent = new Intent(this, Comments_Image.class);
        startActivity(intent);
    }


    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();//go privious activity

        return super.onSupportNavigateUp();
    }
}
