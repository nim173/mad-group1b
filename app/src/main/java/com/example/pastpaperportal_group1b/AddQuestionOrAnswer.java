package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pastpaperportal_group1b.ui.main.Question;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddQuestionOrAnswer extends AppCompatActivity {

    public static final String ID = "pushId";
    public static final String USER = "userId";
    public static final String FROM_ADD = "from add";
    public static final String FROM_EDIT = "from edit";

    private EditText editTitle;
    private EditText editBody;
    private String edit;
    private String pushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_or_answer);

        Intent intent = getIntent();
        TextView textView = findViewById(R.id.forum_header2);
        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        edit = intent.getStringExtra( ViewQuestion.EDIT );

        if("true".equals( edit )){
            editTitle.setText( intent.getStringExtra( ViewQuestion.TITLE ) );
            editBody.setText( intent.getStringExtra( ViewQuestion.BODY ) );
            pushId = intent.getStringExtra( ViewQuestion.PUSH_ID );
        } else {
            String heading = intent.getStringExtra(Forum.HEADER_KEY);
            textView.setText(heading);
        }

        editTitle.setOnFocusChangeListener( (v, hasFocus) -> {
            TextView title = findViewById(R.id.title);
            if (hasFocus) {
                title.setVisibility(View.VISIBLE);
            }
            else {
                title.setVisibility(View.INVISIBLE);
            }
        } );

        editBody.setOnFocusChangeListener( (v, hasFocus) -> {
            TextView title = findViewById(R.id.body);
            if (hasFocus) {
                title.setVisibility(View.VISIBLE);
            }
            else {
                title.setVisibility(View.INVISIBLE);
            }
        } );
    }

    public void submit(View view) {
        DatabaseReference dbRef;
        Question question  = new Question();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Snackbar.make(findViewById(android.R.id.content), "Please sign in", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(255, 174, 66))
                    .setAction("Sign In", v1 -> {
                        Context context = v1.getContext();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);
                    }).setActionTextColor(Color.rgb(0,0,0)).show();
        } else {
            String username = currentUser.getDisplayName();
            String uid = currentUser.getUid();
            dbRef = FirebaseDatabase.getInstance().getReference( "Forum/Question" );

            if(TextUtils.isEmpty(editTitle.getText().toString().trim())) {
                Snackbar.make(findViewById(android.R.id.content), "Title cannot be empty!!", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(179,58,58)).show();
            } else {
                if("true".equals( edit )){
                    dbRef.child( pushId ).child( "title" ).setValue( editTitle.getText().toString().trim() );
                    dbRef.child( pushId ).child( "body" ).setValue( editBody.getText().toString().trim() );
                    Intent newQuestion = new Intent( this, ViewQuestion.class );
                    newQuestion.putExtra( ID, pushId );
                    newQuestion.putExtra( USER, uid );
                    newQuestion.putExtra( FROM_EDIT, "true");
                    startActivity( newQuestion );
                } else {
                    question.setTitle( editTitle.getText().toString().trim() );
                    question.setBody( editBody.getText().toString().trim() );
                    question.setUsername( username );
                    question.setUid( uid );
                    Uri uri = currentUser.getPhotoUrl();
                    if(!(uri == null)) {
                        question.setPhotoUrl(Objects.requireNonNull(uri.toString()));
                    }
                    Date date = new Date();
                    question.setDate( new SimpleDateFormat( "dd-MM-yyyy", Locale.getDefault() ).format( date ) );
                    question.setTime( new SimpleDateFormat( "HH:mm", Locale.getDefault() ).format( date ) );

                    DatabaseReference newRef = dbRef.push();
                    String push = newRef.getKey();
                    newRef.setValue( question );
                    Intent newQuestion = new Intent(this, ViewQuestion.class );
                    newQuestion.putExtra( ID, push );
                    newQuestion.putExtra( USER, uid );
                    newQuestion.putExtra( FROM_ADD, "true");
                    startActivity( newQuestion );
                }
            }
        }
    }
}