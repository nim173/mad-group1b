package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AfterUpload extends AppCompatActivity {

    private DatabaseReference dbRef;
    private String pushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_upload);

        Intent intent = new Intent();
        pushId = intent.getStringExtra(UploadOrEdit.ID);

            dbRef = FirebaseDatabase.getInstance().getReference("UploadPaper/PastPaper" + '/' + pushId);

            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        System.out.println("1");
                        TextView faculty = findViewById(R.id.Faculty);
                        TextView Specialization = findViewById(R.id.specialization);

                        if (dataSnapshot.child("faculty").getValue() != null) {
                            System.out.println("1");
                            faculty.setText(dataSnapshot.child("faculty").getValue().toString());
                        } else {
                            faculty.setText("fail");
                        }

                        if (dataSnapshot.child("Specialization").getValue() != null) {
                            Specialization.setText(dataSnapshot.child("Specialization").getValue().toString());
                            Specialization.setText("testing first loop");
                        } else {
                            Specialization.setText("fail");
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());

                }
            });
        }

    public void Edit(View view){
        Intent intentUpload =  new Intent(this, UploadOrEdit.class);
        Button uploadButton = findViewById(R.id.EditButton);
        startActivity(intentUpload);
    }

    public void delete(View view) {

    }
}
