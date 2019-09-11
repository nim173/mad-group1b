package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class SearchCommon extends AppCompatActivity {

    private EditText mSearchField;
    private ImageView mSearchBtn;
    private DatabaseReference mUserDatabase;
    public static final String MOD_ID = "moduleId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Module");

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageView) findViewById(R.id.searchBtn);

//        mResultList = (RecyclerView) findViewById(R.id.result_list);
//        mResultList.setHasFixedSize(true);
//        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Map<String,String> myMap = new HashMap<String,String>();
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

                AutoCompleteTextView ACTV= (AutoCompleteTextView)findViewById(R.id.search_field);
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
                context.startActivity(intent);

                //firebaseUserSearch(searchText);

            }
        });
    }

   /*private void firebaseUserSearch(String searchText) {

        Toast.makeText(SearchCommon.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {


                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getStatus(), model.getImage());

            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String userName, String userStatus, String userImage){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).into(user_image);


        }

    }*/

    public void onClick(View view){
        Intent intent = new Intent(this, PapersAfterSearch.class);
        startActivity(intent);
    }

    public void toModule(String moduleId){
        Intent intent = new Intent(this, PapersAfterSearch.class);
        intent.putExtra(MOD_ID, moduleId);
        startActivity(intent);
    }
}
