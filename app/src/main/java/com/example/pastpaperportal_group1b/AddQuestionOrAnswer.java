package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddQuestionOrAnswer extends AppCompatActivity {

    public static final String ID = "pushId";
    public static final String USER = "userId";

    private FirebaseAuth mAuth;
    private EditText editTitle;
    private EditText editBody;
    private DatabaseReference dbRef;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_or_answer);

        Intent intent = getIntent();
        String heading = intent.getStringExtra(Forum.HEADER_KEY);
        TextView textView = findViewById(R.id.forum_header2);
        textView.setText(heading);

        mAuth = FirebaseAuth.getInstance();

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        question  = new Question();


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

    public void submit(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();        // These will be added after login integration
        if (currentUser == null) {
            Toast.makeText(getApplicationContext(), "Please sign in", Toast.LENGTH_SHORT).show();
        } else {
            String username = currentUser.getDisplayName();
            String uid = currentUser.getUid();
            //String username = "User123";
            if(TextUtils.isEmpty(editTitle.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "Please fill in a suitable title", Toast.LENGTH_SHORT).show();
            } else {
                Date date = new Date();

                question.setTitle(editTitle.getText().toString().trim());
                question.setBody(editBody.getText().toString().trim());
                question.setUsername(username);
                question.setUid(uid);
                question.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date));
                question.setTime(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));

                dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question");
                DatabaseReference newRef = dbRef.push();
                String pushId = newRef.getKey();
                newRef.setValue(question);

                Toast.makeText(getApplicationContext(), "Question successfully added", Toast.LENGTH_SHORT).show();

                Intent newQuestion = new Intent(this, ViewQuestion.class);
                newQuestion.putExtra(ID, pushId);
                newQuestion.putExtra(USER, uid);
                startActivity(newQuestion);
            }
        }
    }
}
