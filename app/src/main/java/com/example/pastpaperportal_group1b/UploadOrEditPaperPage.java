package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadOrEditPaperPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_or_edit_paper_page);
    }

    public void uploadFinal(View view){
        Intent intent =  new Intent(this, AfterUpload.class);
        Button uploadFinal = findViewById(R.id.UploadEdit);
        startActivity(intent);
    }
}
