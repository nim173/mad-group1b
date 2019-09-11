package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.Module;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchResult extends AppCompatActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Module");
    private RecyclerView mResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String searchText = intent.getStringExtra(SearchCommon.SEARCH);
        //firebaseUserSearch(searchText);
    }

    /*private void firebaseUserSearch(String searchText) {

        Toast.makeText(SearchResult.this, "Started Search", Toast.LENGTH_LONG).show();

        Query searchQuery = dbRef.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Module, ModuleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Module, ModuleViewHolder>(
                Module.class,
                R.layout.list_layout,
                ModuleViewHolder.class,
                searchQuery

        ) {

            @NonNull
            @Override
            public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull ModuleViewHolder moduleViewHolder, int i, @NonNull Module module) {
                moduleViewHolder.setDetails(module.getName());
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ModuleViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(String name){

            TextView nameView = mView.findViewById(R.id.name_text);
            //TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            //ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            nameView.setText(name);
            //user_status.setText(userStatus);

           // Glide.with(ctx).load(userImage).into(user_image);


        }

    }*/
}
