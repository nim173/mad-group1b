package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {

    EditText  pass,Email;
    Button   signup;
//     FirebaseAuth.AuthStateListener mAuthListener;
     FirebaseAuth mAuth;
     ProgressDialog progressDialog;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Email=findViewById(R.id.regiEmail);
        pass=findViewById(R.id.regiPassword);


        mAuth= FirebaseAuth.getInstance();
        signup=findViewById(R.id.btnSignUp);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering com.example.pastpaperportal_group1b.User..");

        //handle register button click

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=Email.getText().toString().trim();
                String password=pass.getText().toString().trim();

                //validation

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    Email.setError("Invalid Email");
                    Email.setFocusable(true);

                }
                else if(password.length()<6){

                    pass.setError("Password length at least 6 characters");
                    pass.setFocusable(true);

                }
                else{
                    registorUser(email,password);
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
        TextView mtxtSignIn =  findViewById(R.id.txtSignIn);
        mtxtSignIn.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(SignUp.this, "Registed..\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this,Profile.class));
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
