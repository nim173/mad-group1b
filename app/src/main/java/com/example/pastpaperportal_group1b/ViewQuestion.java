package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.Replies;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ViewQuestion extends AppCompatActivity {

    public static final String EDIT = "edit";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String PUSH_ID = "id";
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String pushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        pushId = intent.getStringExtra(AddQuestionOrAnswer.ID);
        if (pushId != null) {
            //String uid = intent.getStringExtra(AddQuestionOrAnswer.USER);     //for profile picture

            dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question/" + '/' + pushId);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        TextView title = findViewById(R.id.title);
                        TextView body = findViewById(R.id.editBody);
                        TextView username = findViewById(R.id.username);
                        TextView dateTime = findViewById(R.id.dateTime);
                        Date date = new Date();

                        if (dataSnapshot.child("title").getValue() != null) {
                            title.setText( Objects.requireNonNull( dataSnapshot.child( "title" ).getValue() ).toString());
                            username.setText( Objects.requireNonNull( dataSnapshot.child( "username" ).getValue() ).toString());
                            if ((new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)).equals( Objects.requireNonNull( dataSnapshot.child( "date" ).getValue() ).toString())) {
                                dateTime.setText( Objects.requireNonNull( dataSnapshot.child( "time" ).getValue() ).toString());
                            } else {
                                dateTime.setText( Objects.requireNonNull( dataSnapshot.child( "date" ).getValue() ).toString());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Empty string error", Toast.LENGTH_SHORT).show();
                        }

                        if (dataSnapshot.child("body").getValue() != null) {
                            body.setText( Objects.requireNonNull( dataSnapshot.child( "body" ).getValue() ).toString());
                        } else {
                            body.setText("");
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }

    public void submit(View view){
        final EditText taskEditText = new EditText(this);
        taskEditText.setMinLines(3);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a reply")
                .setView(taskEditText)
                .setPositiveButton("Add", (dialog1, which) -> {
                    FirebaseUser currentUser = mAuth.getCurrentUser();        // These will be added after login integration
                    if (currentUser == null) {
                        Toast.makeText(getApplicationContext(), "Please sign in", Toast.LENGTH_SHORT).show();
                    } else {
                        String uid = currentUser.getUid();
                        Date date = new Date();
                        dbRef = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + pushId);
                        Replies rp = new Replies();
                        rp.setBody(taskEditText.getText().toString());
                        rp.setUid(uid);
                        rp.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date));
                        rp.setTime(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
                        dbRef.push().setValue(rp);
                        Toast.makeText(getApplicationContext(), "Reply added successfully", Toast.LENGTH_SHORT).show();
                    }
                } )
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void onDelete(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setView(null)
                .setPositiveButton("Delete", (dialog1, which) -> {
                    dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question").child( pushId );
                    dbRef.removeValue();
                    Toast.makeText( getApplicationContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } )
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    public void onEdit(View view) {
        Intent intent = new Intent(this, AddQuestionOrAnswer.class );
        TextView title = findViewById( R.id.title);
        TextView body = findViewById( R.id.editBody);
        intent.putExtra( EDIT, "true");
        intent.putExtra( TITLE, title.getText() );
        intent.putExtra( BODY,  body.getText());
        intent.putExtra( PUSH_ID, pushId );
        startActivity( intent );
    }
}