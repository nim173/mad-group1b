package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class Profile extends AppCompatActivity {


    EditText pemail,pName;
    ProgressDialog progressDialog;


    //fire base
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        FirebaseUser curUser=FirebaseAuth.getInstance().getCurrentUser();

        pName=findViewById(R.id.edName);
        pemail=findViewById(R.id.edEmail);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Change PAssword");


        if(curUser.getPhotoUrl() !=null){
            displayImage(curUser.getPhotoUrl());
        }




        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");


        Query query=databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until requierd data

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                   //String name=""+ds.child("name").getValue();
                    String email=""+ds.child("email").getValue();
                    //String pimage=""+ds.child("image").getValue();



                    //setdata


                   // pName.setText(name);
                    pemail.setText(email);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void displayImage(Uri photoUrl) {

        // show The Image in a ImageView
        new DownloadImageTask((ImageView) findViewById(R.id.profileImage))
                .execute(photoUrl.toString());

    }


    private void checkUserStatus(){
        //get current user

        FirebaseUser user =firebaseAuth.getCurrentUser();

        if (user !=null){
            //user is signed in stay here

            pName.setText(user.getDisplayName());
            pemail.setText(user.getEmail());




        }
        else{
            //user  not signed in goto Main Activity

            startActivity(new Intent(Profile.this,MainActivity.class));
        }

    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id ==R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    //change passeword
    public void toConfirmPassword(View view){
        showRecoverPassWordDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class DownloadImageTask extends AsyncTask<String,Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage){
            this.bmImage=bmImage;
        }

        protected  Bitmap doInBackground(String... urls){

            String urldisplay =urls[0];
            Bitmap bitmap =null;


            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }


    }


    private void showRecoverPassWordDialog() {

        //Alter Dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Changing Password");
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

        builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
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
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(!task.isSuccessful()){
                    Toast.makeText(Profile.this, "Faield", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Profile.this, "Sent... ", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(Profile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }






    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();//go privious activity

        return super.onSupportNavigateUp();
    }





    }





