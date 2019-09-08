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
import android.widget.Toast;

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
    public static final String ID2 = "pushId";

    private EditText faculty;
    private EditText Specialization;
    private EditText PaperId;
    private EditText academicYear;
    private EditText semester;
    private EditText module;
    private EditText note;
    private Button uploadButton;
    private EditText pdfName;
    private ProgressBar progressBar;

    DatabaseReference dbRef; //store uploaded files
    StorageReference storageRef; //used for uploading files
    private PaperUpload paperUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_or_edit);
        PaperId = (EditText) findViewById(R.id.PaperId);
        academicYear = (EditText) findViewById(R.id.academ);
        semester = (EditText) findViewById(R.id.sem);
        module = (EditText) findViewById(R.id.mod);
        note = (EditText) findViewById(R.id.note);
        faculty = (EditText) findViewById(R.id.facUp);
        Specialization = (EditText) findViewById(R.id.specialUp);
        paperUpload = new PaperUpload();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        uploadButton = (Button) findViewById(R.id.selectFile);
/*        show = (TextView) findViewById(R.id.show);*/
        pdfName = (EditText) findViewById(R.id.pdfName);


        storageRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference("UploadPaper/");

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectFile();
            }
        });
    }

    private void selectFile() {

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT); //FETCH FILES
                startActivityForResult(Intent.createChooser(intent, "Select a PDF"), 1);
                startActivityForResult(Intent.createChooser(intent, "Select PDF file"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode ==1 && resultCode == RESULT_OK && data !=null && data.getData() !=null) {

            uploadFile(data.getData());

        }
    }

//this has a problem arghhhhhhhhhhhhhhhhhhhh
    private void uploadFile(Uri data) {
         final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setTitle("Loading...");
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
        dbRef = FirebaseDatabase.getInstance().getReference("UploadPaper/PastPaper");

        if(TextUtils.isEmpty(faculty.getText().toString()))
            Toast.makeText(getApplicationContext(), "Enter faculty", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(module.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter related module", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(Specialization.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter related specialization", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(academicYear.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter related academic year", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(semester.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter related semester", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(note.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter any note", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(PaperId.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter a name to show others", Toast.LENGTH_SHORT).show();
        else {
            paperUpload.setFaculty(faculty.getText().toString().trim());
            paperUpload.setSpecialization(Specialization.getText().toString().trim());
            paperUpload.setAcademicYear(academicYear.getText().toString().trim());
            paperUpload.setNote(note.getText().toString().trim());
            paperUpload.setSemester(semester.getText().toString().trim());
            paperUpload.setModule(module.getText().toString().trim());
            paperUpload.setPaperId(PaperId.getText().toString().trim());

            DatabaseReference next = dbRef.push();
            String pushId = next.getKey();
            next.setValue(paperUpload);

            Intent newPaper = new Intent(this, AfterUpload.class);
            newPaper.putExtra(ID, pushId);
            System.out.println("**************************" +pushId);
            startActivity(newPaper);
        }
    }




    }




