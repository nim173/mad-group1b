package com.example.pastpaperportal_group1b;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.PaperUpload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadOrEdit extends AppCompatActivity{

    public static final String ID = "pushId";
    private static final Object MOD_ID = "moduleId";

    private EditText PaperId;
    private Spinner academicYear;
    private Spinner semester;
    private EditText moduleId;
    private EditText note;
    private Button uploadButton;
    private EditText pdfName;
    private ProgressBar progressBar;

   private String pushId;
            DatabaseReference dbRef; //store uploaded files
    StorageReference storageRef; //used for uploading files
    private PaperUpload paperUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_or_edit);
        PaperId = (EditText) findViewById(R.id.PaperId);
        moduleId = (EditText) findViewById(R.id.mod);
        note = (EditText) findViewById(R.id.pdfName);
        paperUpload = new PaperUpload();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        academicYear = findViewById(R.id.spinner);

        uploadButton = (Button) findViewById(R.id.selectFile);
/*        show = (TextView) findViewById(R.id.show);*/
        pdfName = (EditText) findViewById(R.id.pdfName);


        Intent intent = getIntent();
        pushId = intent.getStringExtra(PapersAfterSearch.PAPER_ID);
        System.out.println("nimmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"+pushId);

        storageRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference("UploadPaper/");

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("______________________________________________________ENTERED1");

                selectFile();



            }
        });
    }

    private void selectFile() {
        System.out.println("______________________________________________________ENTERED2");
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT); //FETCH FILES
                //startActivityForResult(Intent.createChooser(intent, "Select a PDF"), 1);
                startActivityForResult(Intent.createChooser(intent, "Select PDF file"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("______________________________________________________ENTERED3");
        if(requestCode ==1 && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
            System.out.println("______________________________________________________ENTERED4");
            uploadFile(data.getData());
        }
    }

    private void uploadFile(Uri data) {
        System.out.println("__________________________________________________ENTERED5");
         final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setTitle("Uploading...");
         progressDialog.show();

        StorageReference reference = storageRef.child("UploadPaper/PastPaper/PDF" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        paperUpload = new PaperUpload(pdfName.getText().toString(), url.toString());
                        dbRef.child(dbRef.push().getKey()).setValue(paperUpload);
                        Toast.makeText(UploadOrEdit.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded: " +(int) progress + "%");
            }
        });
    }

    public void upload(View view) {



        System.out.println("666666666666666666666666666666666666666666" + pushId);

        dbRef = FirebaseDatabase.getInstance().getReference("Module/" +  pushId + "/" +
                academicYear.getSelectedItem().toString().trim() + "/" + PaperId.getText().toString().trim()).child("url");

        System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkk    "+ dbRef);

         if (TextUtils.isEmpty(note.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter any note", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(PaperId.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter a name to show others", Toast.LENGTH_SHORT).show();
        else {
            paperUpload.setNote(note.getText().toString().trim());
            paperUpload.setModuleId(pushId);
             academicYear.getSelectedItem().toString().trim();
             paperUpload.setPaperId(PaperId.getText().toString().trim());
             dbRef.setValue(paperUpload.getPdfName());

            DatabaseReference next = dbRef.push();
            /* pushId = next.getKey();*/
            next.setValue(paperUpload);

                            //here module is a push id(as module names can be the same for different stuff,
                            // this module id or whatever will be taken as an intent
                            //after search
                            //  year is 2018,2019, etc
                            // year 1,2 will be added by mr when adding modules
                            // you need to either check if paper id/name (ex: june) isnt duplicated , because it will overwrite when adding a new one
                            // or set a push id, so that papers can have the same name,  but you will have to keep using the push id to reference that paper
                            // which will be a pain in the ass

            Intent newPaper = new Intent(this, ViewPaper.class);
            newPaper.putExtra(ID, pushId);
            System.out.println("**************************" +pushId);
            startActivity(newPaper);
        }
    }




    }




