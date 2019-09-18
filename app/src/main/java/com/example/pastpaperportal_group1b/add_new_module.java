package com.example.pastpaperportal_group1b;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.Module;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class add_new_module extends AppCompatActivity {

    EditText mname;
    EditText mabbr;
    EditText mkey;
    ImageButton mmodulebtn;

    List<Module> modules;
    DatabaseReference databaseModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_module);

        databaseModules = FirebaseDatabase.getInstance().getReference("Module");

        mname = (EditText) findViewById(R.id.module_name);
        mabbr = (EditText) findViewById(R.id.module_abbr);
        mkey = (EditText) findViewById(R.id.enroll_key);

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
        String m_abbr = mabbr.getText().toString();
        String m_key = mkey.getText().toString();

        if (!TextUtils.isEmpty(m_name)) {
            String m_pid = databaseModules.push().getKey();
            Module module = new Module();
            module.setName(m_name);
            module.setAbbrev(m_abbr);
            module.setKey(m_key);
            module.setPushId(m_pid);
            databaseModules.child(m_pid).setValue(module);
            Toast.makeText(this, "Message Added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Please enter module name",Toast.LENGTH_LONG).show();
        }

    }
}
