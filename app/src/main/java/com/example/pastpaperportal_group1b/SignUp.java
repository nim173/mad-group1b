package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView mtxtSignIn =  findViewById(R.id.txtSignIn);
        mtxtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent login=new Intent(SignUp.this, Login.class);
              startActivity(login);

            }
        });


    }

    public void toComments_Images(View view){
        Intent intent = new Intent(this, Comments_Image.class);
        startActivity(intent);
    }
}
