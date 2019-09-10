package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
*/

public class ViewPaper extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayout linearLayout;
    ExpandableRelativeLayout answers;

    private DatabaseReference dbRef;
    private String pushId;

    public static final String FORUM_KEY = "PASTPAPERPORTAL.FORUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paper);

        Intent intent = getIntent();

        pushId = intent.getStringExtra(UploadOrEdit.ID);
        System.out.println("22222$$$$$$$$$$$$$$$$$$$$" + UploadOrEdit.ID);
        System.out.println("222222222##########################################" +pushId);


        dbRef = FirebaseDatabase.getInstance().getReference("UploadPaper/PastPaper" + '/' + pushId);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    TextView PaperId = findViewById(R.id.paperId);

                    if (dataSnapshot.child("PaperId").getValue() != null) {
                        PaperId.setText(dataSnapshot.child("PaperId").getValue().toString());
                    } else {
                        PaperId.setText("fail");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });
    }


    public void showAnswers(View view) {

        answers = (ExpandableRelativeLayout) findViewById(R.id.answers);
        answers.toggle();

    }
    public void uploadAnswer(View view){
        Intent intentUpload =  new Intent(this, UploadOrEdit.class);
        Button uploadButton = findViewById(R.id.uploadButton);
        startActivity(intentUpload);
    }

    public void goToForum(View view){
        Intent intentForum = new Intent(this, Forum.class);
        TextView textView5 = findViewById(R.id.textView5);                  //@Dinuli change textView5 to have the module name as well maybe?
        String heading = textView5.getText().toString();
        intentForum.putExtra(FORUM_KEY, heading);
        startActivity(intentForum);
    }
}
