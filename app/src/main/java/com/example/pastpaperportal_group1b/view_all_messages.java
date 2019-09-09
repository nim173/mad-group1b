package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pastpaperportal_group1b.ui.main.FirebaseDatabaseHelper;
import com.example.pastpaperportal_group1b.ui.main.Messages;

import java.util.List;

public class view_all_messages extends AppCompatActivity {

    private RecyclerView mRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_messages);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview_messages);
        new FirebaseDatabaseHelper().readMessages(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Messages> Messages, List<String> Keys) {
                findViewById(R.id.loading_msgs_pb).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerview,view_all_messages.this,Messages,Keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }


}
