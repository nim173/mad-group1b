package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewPaper extends AppCompatActivity {

    public static final String FORUM_KEY = "PASTPAPERPORTAL.FORUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paper);
    }

    public void uploadAnswer(View view){
        Intent intentAnswer =  new Intent(this, UploadOrEditPaperPage.class);
        Button uploadButton = findViewById(R.id.uploadButton);
        startActivity(intentAnswer);
    }

    public void goToForum(View view){
        Intent intentForum = new Intent(this, Forum.class);
        TextView textView5 = findViewById(R.id.textView5);                  //@Dinuli change textView5 to have the module name as well maybe? yeah that's cool
        String heading = textView5.getText().toString();
        intentForum.putExtra(FORUM_KEY, heading);
        startActivity(intentForum);
    }
}
