package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadOrEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_or_edit);
    }


    public void uploadAnswer(View view){
        Intent intentUpload =  new Intent(this, AfterUpload.class);
        Button uploadButton = findViewById(R.id.uploadEdit);
        startActivity(intentUpload);
    }


}
