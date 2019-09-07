package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadOrEdit extends AppCompatActivity {

    public static final String ID = "pushId";
    public static final String USER = "userId";

    private EditText faculty;
    private EditText Specialization;
    DatabaseReference dbRef;
    private PaperUpload paperUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_or_edit);

        faculty = (EditText) findViewById(R.id.facUp);
        Specialization = (EditText) findViewById(R.id.specialUp);
        paperUpload = new PaperUpload();
    }

    public void upload(View view) {


        dbRef = FirebaseDatabase.getInstance().getReference("UploadPaper/PastPaper");

        if(TextUtils.isEmpty(faculty.getText().toString()))
            Toast.makeText(getApplicationContext(), "Enter faculty", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(Specialization.getText().toString()))
            Toast.makeText(getApplicationContext(), "enter related specialization", Toast.LENGTH_SHORT).show();
        else {
            paperUpload.setFaculty(faculty.getText().toString().trim());
            paperUpload.setSpecialization(Specialization.getText().toString().trim());

            DatabaseReference next = dbRef.push();
            String pushId = next.getKey();
            next.setValue(paperUpload);


            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();


            Intent newPaper = new Intent(this, AfterUpload.class);
            newPaper.putExtra(ID, pushId);
            startActivity(newPaper);
        }
    }
/*


    public void uploadAnswer(View view){
        Intent intentUpload =  new Intent(this, AfterUpload.class);
        Button uploadButton = findViewById(R.id.uploadEdit);
        startActivity(intentUpload);
    }

    public void download (View view) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF);
    }
*/

}
