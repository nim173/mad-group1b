package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class admin_dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_dashboard);
    }

    public void onclick(View view){
        startActivity(new Intent(this,manage_users.class));
    }

    public void sendMsg(View view) {
        startActivity(new Intent(this, send_message.class));
    }

    public void view_allmodules(View view) {
        startActivity(new Intent(this,view_all_modules.class));
    }

    public void addnewModule(View view){
        startActivity(new Intent(this,add_new_module.class));
    }

}
