package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.FirebaseDatabaseHelper;
import com.example.pastpaperportal_group1b.ui.main.Messages;

import java.util.List;

public class send_message extends AppCompatActivity {

    private EditText Subject;
    private EditText Body;
    private Spinner sentTo;
    private EditText userId;
    private EditText author;

    private ImageButton sendbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Subject = (EditText) findViewById(R.id.subject);
        Body = (EditText) findViewById(R.id.body);
        sentTo = (Spinner) findViewById(R.id.sent_to);
        userId = (EditText) findViewById(R.id.userId);

        sendbtn = (ImageButton) findViewById(R.id.sendbtn);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Messages messages = new Messages();
                messages.setSubject(Subject.getText().toString());
                messages.setBody(Body.getText().toString());
                messages.setSent_To(sentTo.getSelectedItem().toString());
                messages.setUserId(userId.getText().toString());
                new FirebaseDatabaseHelper().addMessage(messages, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Messages> Messages, List<String> Keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(send_message.this,"Message Sent Successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

    }

    public void gotoAll(View view){

        Intent intent = new Intent(this, view_all_messages.class);
        startActivity(intent);

    }

}
