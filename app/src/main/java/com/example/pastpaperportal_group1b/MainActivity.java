package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pastpaperportal_group1b.IT18125658.Forum.FirebaseUIActivity;
import com.example.pastpaperportal_group1b.Search.SearchCommon;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SearchCommon.class);
        startActivity(intent);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, SearchCommon.class);
        startActivity(intent);
    }

    public void toSignUp(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void toadmin(View view) {
        Intent intent = new Intent(this, admin_dashboard.class);
        startActivity(intent);
    }

    public void toFire(View view){
        Intent intent = new Intent(this, FirebaseUIActivity.class);
        startActivity(intent);
    }
}
