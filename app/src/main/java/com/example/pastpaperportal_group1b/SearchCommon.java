package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SearchCommon extends AppCompatActivity {

    private EditText mSearchField;
    public static final String MOD_ID = "moduleId";
    public static final String SEARCH = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);

        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference("Module");
        mSearchField = (EditText) findViewById(R.id.search_field);
        ImageView mSearchBtn = (ImageView) findViewById(R.id.searchBtn);

        Map<String,String> myMap = new HashMap<>();
        final ArrayAdapter<String> modules = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modules.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String add = snapshot.child("name").getValue(String.class) + " - " + snapshot.child("key").getValue(String.class);
                    modules.add(add);
                    myMap.put(snapshot.getKey(), add);
                }

                AutoCompleteTextView ACTV= findViewById(R.id.search_field);
                ACTV.setAdapter(modules);

                ACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        String value = textView.getText().toString();
                        String moduleId = null;
                        for (String s : myMap.keySet()) {       // assuming different modules cant have same name and key
                            if (myMap.get(s).equals(value)) {
                                moduleId = s;
                                Toast.makeText(SearchCommon.this, s, Toast.LENGTH_SHORT).show();
                                toModule(moduleId);
                                break;
                            }
                        }
                        if(moduleId == null){
                            Toast.makeText(SearchCommon.this, "Module not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();
                Context  context = view.getContext();
                Intent intent = new Intent(context, SearchResult.class);
                intent.putExtra(SEARCH, searchText);
                context.startActivity(intent, ActivityOptions
                        .makeSceneTransitionAnimation((Activity) context).toBundle());
            }
        });
    }

    public void onClick(View view){
        Intent intent = new Intent(this, PapersAfterSearch.class);
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(this).toBundle());
    }

    public void toModule(String moduleId){
        Intent intent = new Intent(this, PapersAfterSearch.class);
        intent.putExtra(MOD_ID, moduleId);
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(this).toBundle());
    }
}
