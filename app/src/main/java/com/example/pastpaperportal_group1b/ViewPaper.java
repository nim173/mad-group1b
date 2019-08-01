package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewPaper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paper);
    }

    public void uploadAnswer(View view){
        Intent intentUpload =  new Intent(this, AfterUpload.class);
        Button uploadButton = findViewById(R.id.uploadButton);
        startActivity(intentUpload);
    }
}
