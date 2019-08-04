package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddQuestionOrAnswer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_or_answer);

        Intent intent = getIntent();
        String heading = intent.getStringExtra(Forum.HEADER_KEY);
        TextView textView = findViewById(R.id.forum_header2);
        textView.setText(heading);

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editBody = findViewById(R.id.editBody);

        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                TextView title = findViewById(R.id.title);
                if (hasFocus) {
                    title.setVisibility(View.VISIBLE);
                }
                else {
                    title.setVisibility(View.INVISIBLE);
                }
            }
        });

        editBody.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                TextView title = findViewById(R.id.body);
                if (hasFocus) {
                    title.setVisibility(View.VISIBLE);
                }
                else {
                    title.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}
