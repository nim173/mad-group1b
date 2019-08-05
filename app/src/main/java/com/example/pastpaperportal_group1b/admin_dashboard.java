package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class admin_dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
    }

    public void onclick(View view){

        Intent intent = new Intent(this, manage_users.class);
        startActivity(intent);

    }

    public void onrequests(View view) {
        Intent intent = new Intent(this, requests.class);
        startActivity(intent);
    }


    public void sendMsg(View view) {

        Intent intent = new Intent(this, send_message.class);
        startActivity(intent);
    }
}