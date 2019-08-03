package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterUpload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_upload);
    }

    public void EditUpload(View view){
        Intent intentUpload =  new Intent(this, UploadOrEditPaperPage.class);
        Button uploadButton = findViewById(R.id.EditButton);
        startActivity(intentUpload);
    }
}
