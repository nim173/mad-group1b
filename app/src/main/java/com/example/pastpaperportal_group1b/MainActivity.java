package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, SearchCommon.class);
        startActivity(intent);
    }

    public void toSignUp(View view){
        Intent intent = new Intent(this, SignUp.class);
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
