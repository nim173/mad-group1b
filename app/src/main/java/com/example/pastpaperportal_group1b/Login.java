package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView toSignUp= findViewById(R.id.body);
        TextView tochangepsw= findViewById(R.id.title);


        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup=new Intent(Login.this,SignUp.class);
                startActivity(signup);
            }
        });

        tochangepsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePws=new Intent(Login.this,ChangedPassword.class);
                startActivity(changePws);
            }
        });

    }
}
