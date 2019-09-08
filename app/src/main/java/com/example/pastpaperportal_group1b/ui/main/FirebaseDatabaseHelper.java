package com.example.pastpaperportal_group1b.ui.main;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceMessages;
    private List<Messages> Messages = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Messages> Messages, List<String> Keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceMessages = mDatabase.getReference("Messages");
    }

    public void readMessages(final DataStatus dataStatus) {
        mReferenceMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Messages.clear();
                    List<String> Keys = new ArrayList<>();
                    for (DataSnapshot KeyNode : dataSnapshot.getChildren()){
                        Keys.add(KeyNode.getKey());
                        Messages message = KeyNode.getValue(Messages.class);
                        Messages.add(message);
                    }
                    dataStatus.DataIsLoaded(Messages,Keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addMessage(Messages messages,final DataStatus dataStatus) {
        String key = mReferenceMessages.push().getKey();
        mReferenceMessages.child(key).setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateMessage(String key,Messages messages,final DataStatus dataStatus) {
        mReferenceMessages.child(key).setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteMessage(String key,final DataStatus dataStatus) {
        mReferenceMessages.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
