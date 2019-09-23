package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.FirebaseDatabaseHelper;
import com.example.pastpaperportal_group1b.ui.main.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class send_message extends AppCompatActivity {

    private EditText Subject;
    private EditText Body;
    private Spinner sentTo;
    private EditText userId;

    private Button sendbtn;

    //FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //currentUser = mAuth.getCurrentUser();
    //String username = currentUser.getDisplayName();
    //String url = currentUser.getPhotoUrl().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_send_message);
        Subject = (EditText) findViewById(R.id.subject);
        Body = (EditText) findViewById(R.id.body);
        sentTo = (Spinner) findViewById(R.id.sent_to);
        userId = (EditText) findViewById(R.id.userId);

        //clear all text fields
        Subject.setText(""); Body.setText(""); sentTo.setTag(""); userId.setText("");

        sendbtn = (Button) findViewById(R.id.sendbtn1);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Messages messages = new Messages();
                messages.setSubject(Subject.getText().toString());
                messages.setBody(Body.getText().toString());
                messages.setSent_To(sentTo.getSelectedItem().toString());
                messages.setUserId(userId.getText().toString());
                messages.setAuthor("Admin");
                //messages.setPhotoUrl(url);
                Date date = new Date();
                messages.setDate(new SimpleDateFormat( "dd-MM-yyyy", Locale.getDefault() ).format( date ) );
                new FirebaseDatabaseHelper().addMessage(messages, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Messages> Messages, List<String> Keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(send_message.this,"Message Sent Successfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(send_message.this,view_all_messages.class));
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });gotoAll(view);
            }
        });

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//go privious activity
        return super.onSupportNavigateUp();
    }

    public void gotoAll(View view){
        startActivity(new Intent(send_message.this,view_all_messages.class));
    }

}
