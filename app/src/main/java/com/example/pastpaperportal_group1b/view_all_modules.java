package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.pastpaperportal_group1b.ui.main.AdapterModule;
import com.example.pastpaperportal_group1b.ui.main.Module;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class view_all_modules extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterModule adapterModule;
    List<Module> moduleList;
    FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_view_all_modules);
        btn = findViewById(R.id.new_module);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view_all_modules.this,add_new_module.class));
            }
        });
        //init recyclerview
        recyclerView = (RecyclerView)findViewById(R.id.modulerecycler1);
        findViewById(R.id.pbmodule).setVisibility(View.GONE);
        //set its properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //init user list
        moduleList = new ArrayList<>();
        //get all users

        //get path of database named "Module" containing info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Module");
        //get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                moduleList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Module module = ds.getValue(Module.class);
                    moduleList.add(module);
                    //adapter
                    adapterModule = new AdapterModule(view_all_modules.this, moduleList);
                    //set adapter to recyclerview
                    recyclerView.setAdapter(adapterModule);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("View All Modules");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//go privious activity
        return super.onSupportNavigateUp();
    }
}
