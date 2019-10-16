package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pastpaperportal_group1b.Search.SearchCommon;
import com.google.firebase.auth.FirebaseAuth;

public class admin_dashboard extends AppCompatActivity {

    TextView hellomsg;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_dashboard);
        hellomsg = findViewById(R.id.hellomsg);
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");
        hellomsg.setText(String.format("Hello %s", user_name));
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id ==R.id.action_logout){
            firebaseAuth.signOut();
            startActivity(new Intent(this, SearchCommon.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void onclick(View view){
        Intent intent = new Intent(this,manage_users.class);
        intent.putExtra("user_name","Admin");
        startActivity(intent);
    }

    public void sendMsg(View view) {
        Intent intent = new Intent(this, send_message.class);
        intent.putExtra("user_name","Admin");
        startActivity(intent);
    }

    public void view_allmodules(View view) {
        Intent intent = new Intent(this,view_all_modules.class);
        intent.putExtra("user_name","Admin");
        startActivity(intent);
    }

//    public void addnewModule(View view){
//        Intent intent = new Intent(this,add_new_module.class);
//        intent.putExtra("user_name","Admin");
//        startActivity(intent);
//    }

    public void addpastpaper(View view) {
        Intent intent = new Intent(this,PapersAfterSearch.class);
        intent.putExtra("user_name","Admin");
        startActivity(intent);
    }

    public void discusspaper(View view) {
        Intent intent = new Intent(this,PapersAfterSearch.class);
        intent.putExtra("user_name","Admin");
        startActivity(intent);
    }

    public void searchpapers(View view) {
        Intent intent = new Intent(this,SearchCommon.class);
        intent.putExtra("user_name","Admin");
        startActivity(intent);
    }

}
