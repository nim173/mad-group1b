package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SearchCommon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, PapersAfterSearch.class);
        startActivity(intent);
    }
}