package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PapersAfterSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers_after_search);
    }

    public void ViewDetails(View view) {

        Intent intent = new Intent(this, ViewPaper.class);
        Button viewDetailsButton = (Button) findViewById(R.id.viewDetails);
        startActivity(intent);
    }
}
