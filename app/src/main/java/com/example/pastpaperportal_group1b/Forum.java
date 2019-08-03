package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Forum extends AppCompatActivity {

    //good practice to use the key as capital letters since the data received
    // at the next activity will be eventually treated as immutable
    public static final String HEADER_KEY = "PASTPAPERPORTAL.FORUM.QUESTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        String heading = intent.getStringExtra(ViewPaper.FORUM_KEY);
        TextView textView = findViewById(R.id.forum_header);
        textView.setText(heading);
    }

    public void addQuestion(View view){
        Intent intentQuestion = new Intent(this, AddQuestionOrAnswer.class);
        TextView header = findViewById(R.id.forum_header);                  //@Dinuli change textView5 to have the module name as well maybe?
        String heading = header.getText().toString();
        intentQuestion.putExtra(HEADER_KEY, heading);
        startActivity(intentQuestion);
    }
}
