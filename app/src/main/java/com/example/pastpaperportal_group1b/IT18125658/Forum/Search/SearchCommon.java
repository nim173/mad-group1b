package com.example.pastpaperportal_group1b.IT18125658.Forum.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.Login;
import com.example.pastpaperportal_group1b.PapersAfterSearch;
import com.example.pastpaperportal_group1b.ProfileDefault;
import com.example.pastpaperportal_group1b.R;
import com.example.pastpaperportal_group1b.SignUp;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SearchCommon extends AppCompatActivity {

    private EditText mSearchField;
    public static final String MOD_ID = "moduleId";
    public static final String SEARCH = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_search_common);

        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference("Module");
        mSearchField = findViewById(R.id.search_field);

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

                ACTV.setOnItemClickListener((parent, view, position, id) -> {
                    TextView textView = view.findViewById(android.R.id.text1);
                    String value = textView.getText().toString();
                    String moduleId = null;
                    for (String s : myMap.keySet()) {       // assuming different modules cant have same name and key
                        if (Objects.requireNonNull(myMap.get(s)).equals(value)) {
                            moduleId = s;
                            Toast.makeText(SearchCommon.this, s, Toast.LENGTH_SHORT).show();
                            toModule(moduleId);
                            break;
                        }
                    }
                    if(moduleId == null){
                        Toast.makeText(SearchCommon.this, "Module not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void search(View view){
        mSearchField = findViewById(R.id.search_field);
        String searchText = mSearchField.getText().toString();
        Intent intent = new Intent(this, SearchResult.class);
        intent.putExtra(SEARCH, searchText);
        startActivity(intent);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, PapersAfterSearch.class);
        startActivity(intent);
    }

    private void toModule(String moduleId){
        Intent intent = new Intent(this, PapersAfterSearch.class);
        intent.putExtra(MOD_ID, moduleId);
        startActivity(intent);
    }

    public void toLogin(View view){
        if(checkUser()){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    public void toSignUp(View view){
        if(checkUser()){
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }
    }

    private boolean checkUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Snackbar.make(findViewById(android.R.id.content), "Already logged in!!", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(0, 184, 212)).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id ==R.id.action_profile){
            startActivity(new Intent(SearchCommon.this, ProfileDefault.class));
        }

        return super.onOptionsItemSelected(item);
    }


}
