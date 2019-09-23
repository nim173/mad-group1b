package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.Module;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class add_new_module extends AppCompatActivity {

    EditText mname;
    EditText mabbr;
    EditText mkey;
    ImageButton mmodulebtn;
    Spinner m_year,m_faculty;

    List<Module> modules;
    DatabaseReference databaseModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_new_module);

        databaseModules = FirebaseDatabase.getInstance().getReference("Module");

        mname = (EditText) findViewById(R.id.module_name);
        mabbr = (EditText) findViewById(R.id.module_abbr);
        mkey = (EditText) findViewById(R.id.enroll_key);
        m_faculty = (Spinner) findViewById(R.id.m_faculty);
        m_year = (Spinner) findViewById(R.id.m_year);

        //clear all textfields
        mname.setText(""); mabbr.setText(""); mkey.setText(""); m_faculty.setTag(""); m_year.setTag("");

        mmodulebtn = (ImageButton) findViewById(R.id.add_module);

        modules = new ArrayList<>();

        mmodulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addModule();
            }
        });

    }

    private void addModule() {

        String m_name = mname.getText().toString().trim();
        String m_abbr = mabbr.getText().toString().trim();
        String m_key = mkey.getText().toString().trim();
        String m__faculty = m_faculty.getSelectedItem().toString().trim();
        int m__year =  Integer.parseInt(m_year.getSelectedItem().toString().trim());

        if (TextUtils.isEmpty(m_name))
            Toast.makeText(this,"Please enter module name",Toast.LENGTH_LONG).show();
            else if (TextUtils.isEmpty(m_abbr))
                Toast.makeText(this,"Please enter module abbreviation",Toast.LENGTH_LONG).show();
            else if (TextUtils.isEmpty(m_key))
                Toast.makeText(this,"Please enter module enroll key",Toast.LENGTH_LONG).show();
            else {
            Query getdata = databaseModules.orderByChild("key").equalTo(m_key);
            getdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        String m_pid = databaseModules.push().getKey();
                        Module module = new Module();
                        module.setPushId(m_pid);
                        module.setName(m_name);
                        module.setAbbrev(m_abbr);
                        module.setKey(m_key);
                        module.setFaculty(m__faculty);
                        module.setYear(m__year);
                        HashMap<String, Object> insert = new HashMap<>();
                        insert.put("name", m_name);
                        insert.put("abbrev", m_abbr);
                        insert.put("key", m_key);
                        insert.put("year", m__year);
                        insert.put("faculty", m__faculty);
                        databaseModules.child(m_pid).setValue(insert);
                        Toast.makeText(add_new_module.this, "Module Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(add_new_module.this,view_all_modules.class));
                    } else {
                        Toast.makeText(add_new_module.this, "Enrollment Key exists", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        }
    }
