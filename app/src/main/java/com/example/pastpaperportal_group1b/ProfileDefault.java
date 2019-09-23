package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.HashMap;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;


public class ProfileDefault extends AppCompatActivity {

    //fire base
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;

    StorageReference storageReference;

    String storagePath="Users_Profile_Cover_Imgs/";

    ProgressDialog progressDialog;

    Uri uri_image;

    //permission
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY__CODE=300;
    private static final int IMAGE_PICK_CAMERA__CODE=400;

            String cameraPermissions[];
            String storagePermission[];

            String profileOrcover;

    ImageView avatar,coverIv;
    TextView nameTV,emailtv;
    FloatingActionButton fab;
    Button changepws;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_profile_default);


        FirebaseUser curUser=FirebaseAuth.getInstance().getCurrentUser();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering");

        changepws=findViewById(R.id.btnchangepass);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");


        storageReference=FirebaseStorage.getInstance().getReference();


        //init permission

        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        avatar=(ImageView) findViewById(R.id.avataIv);
        nameTV=findViewById(R.id.txtname);
        emailtv=findViewById(R.id.txtEmail);
        coverIv=(ImageView) findViewById(R.id.coverIv);
        fab=findViewById(R.id.floatingActionButton);





        Query query=databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until requierd data

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String name=""+ds.child("username").getValue();
                    String email=""+ds.child("email").getValue();
                    String url=""+ds.child("url").getValue();
                    String cover=""+ds.child("cover").getValue();

                    //setdata
                    nameTV.setText(name);
                    emailtv.setText(email);

                    try {
                        System.out.println("Profile Image");
                        //Picasso.get().load(url).into(avatar);

                        Picasso.get().load(url).fit().centerCrop().into(avatar);
                    }
                    catch (Exception e){
                        Picasso.get().load(R.drawable.ic_addpic).into(avatar);
                    }

                    try {

                        System.out.println("Cover Image");
                      //  Picasso.get().load(cover).into(coverIv);
                        Picasso.get().load(cover).fit().centerCrop().into(coverIv);
                    }
                    catch (Exception e){


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //floting button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfileDialog();
            }
        });


        changepws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPassWordDialog();
            }
        });

    }

    private boolean checkstoragePermisssion(){

        boolean resilt= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);

        return resilt;
    }

    private void requeststoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }

    //camera

    private boolean checkCameraPermisssion(){

        boolean resilt= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                ==(PackageManager.PERMISSION_GRANTED);

        boolean resilt1= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);

        return resilt && resilt1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }



    private void showEditProfileDialog() {

        String option[]={"Edit photo","Edit Cover","Edit Name"};

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        //title
        builder.setTitle("Choose Action");
        //setItems
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //handle dialog item

                if(i==0){
                    progressDialog.setMessage("Updating profile picture");
                    profileOrcover ="url";
                    showImageProfile();


                }
                else if(i==1){
                    progressDialog.setMessage("Updating Cover picture");
                    profileOrcover="cover";
                    showImageProfile();

                }
                else if(i==2){
                    progressDialog.setMessage("Updating  Name");
                    shaowNameandemailUpdate("username");

                }


            }
        });
        //show dialog
        builder.create().show();




    }

    private void shaowNameandemailUpdate(String key){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Update "+key);
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);


        EditText editText=new EditText(this);
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String values=editText.getText().toString().trim();

                if(!TextUtils.isEmpty(values)){
                    progressDialog.show();
                    HashMap<String,Object>result=new HashMap<>();
                    result.put(key,values);

                    databaseReference.child(firebaseUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileDefault.this, "Updated", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressDialog.dismiss();
                                }
                            });

                }
                else{
                    Toast.makeText(ProfileDefault.this, "Please enter"+key, Toast.LENGTH_SHORT).show();
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();

    }

    private void showImageProfile() {


        String option[]={"Camera", "Gallery"};

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        //title
        builder.setTitle("Pick Image from");
        //setItems
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //handle dialog item

                if(i==0){


                    if(!checkCameraPermisssion()){
                        requestCameraPermission();

                    }
                    else {
                        pickOnCamera();

                    }



                }
                else if(i==1){
                    //gallery
                    if(!checkstoragePermisssion()){
                        requeststoragePermission();
                    }
                    else{
                        pickOnGallery();
                    }



                }


            }
        });
        //show dialog
        builder.create().show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch(requestCode){

            case CAMERA_REQUEST_CODE:{

                if (grantResults.length>0){
                    boolean cameraAccept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccept =grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if(cameraAccept && writeStorageAccept){
                        pickOnCamera();
                    }
                    else{
                        Toast.makeText(this, "Please enable camera and sotrage permission", Toast.LENGTH_SHORT).show();
                    }

                }

            }
            break;

            case STORAGE_REQUEST_CODE:{

                if (grantResults.length>0){
                    //gallery
                    boolean writeStorageAccept =grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if( writeStorageAccept){
                        pickOnGallery();
                    }
                    else{
                        Toast.makeText(this, "Please enable  sotrage permission", Toast.LENGTH_SHORT).show();
                    }

                }

            }
            break;
        }



    }

    //pick on gallery
    private void pickOnGallery() {

        Intent galleryIntent=new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/+");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY__CODE);

    }

    private void pickOnCamera() {

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        uri_image =this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri_image);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA__CODE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode ==RESULT_OK){

            if(requestCode == IMAGE_PICK_GALLERY__CODE){
                uri_image=data.getData();

                uplordProfileCoverPhoto(uri_image);
            }
            if(requestCode == IMAGE_PICK_CAMERA__CODE){

                uplordProfileCoverPhoto(uri_image);

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uplordProfileCoverPhoto(Uri uri_image) {
        progressDialog.show();

        String filepathAndName=storagePath+""+profileOrcover + "_"+ firebaseUser.getUid();

        StorageReference storageReference2nd=storageReference.child(filepathAndName);
        storageReference2nd.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri>uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri downlordUri =uriTask.getResult();

                //check image is uplord or not
                if(uriTask.isSuccessful()){

                    HashMap<String,Object>results=new HashMap<>();
                    results.put(profileOrcover,downlordUri.toString());

                    databaseReference.child(firebaseUser.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileDefault.this, "Image Updated", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(ProfileDefault.this, "Error Updating Image", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                else{
                    progressDialog.dismiss();
                    Toast.makeText(ProfileDefault.this, "Some error occure", Toast.LENGTH_SHORT).show();

                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ProfileDefault.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void checkUserStatus(){
        //get current user

        FirebaseUser user =firebaseAuth.getCurrentUser();

        if (user !=null){
            //user is signed in stay here

            nameTV.setText(user.getDisplayName());
            emailtv.setText(user.getEmail());




        }
        else{
            //user  not signed in goto Main Activity

            startActivity(new Intent(ProfileDefault.this,SearchCommon  .class));
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
        else if(id==R.id.action_delete){

            deleteAccount();

        }


        return super.onOptionsItemSelected(item);
    }

    private void deleteAccount() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(ProfileDefault.this);
        dialog.setTitle("Are You sure");
        dialog.setMessage("Delete this  account will ");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileDefault.this, "Account Delete", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(ProfileDefault.this,SearchCommon.class));
                        }
                        else {
                            Toast.makeText(ProfileDefault.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });



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
                    Toast.makeText(ProfileDefault.this, "Faield", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ProfileDefault.this, "Sent... ", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(ProfileDefault.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();//go privious activity

        return super.onSupportNavigateUp();
    }



}
