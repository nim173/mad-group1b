package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.Module;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.pastpaperportal_group1b.SearchCommon.MOD_ID;

public class SearchResult extends AppCompatActivity {

    private DatabaseReference dbRef;
    private RecyclerView mResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        dbRef = FirebaseDatabase.getInstance().getReference("Module");
        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String searchText = intent.getStringExtra(SearchCommon.SEARCH);
        Toast.makeText(this, searchText, Toast.LENGTH_SHORT).show();
        firebaseUserSearch(searchText);
    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(SearchResult.this, "Started Search", Toast.LENGTH_LONG).show();

        Query searchQuery = dbRef.orderByChild("name");
                //.startAt(searchText).endAt(searchText + "\uf8ff");
        //System.out.println(searchQuery);

        SnapshotParser<Module> snapshotParser = new SnapshotParser<Module>() {

            @Override
            public Module parseSnapshot(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Module.class).getName().toLowerCase().matches(".*" + searchText.toLowerCase() + ".*"))
                    return snapshot.getValue(Module.class);
                else return new Module();
            }
        };

        FirebaseRecyclerOptions<Module> options =
                new FirebaseRecyclerOptions.Builder<Module>()
                        .setQuery(searchQuery, snapshotParser)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Module, ModuleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Module, ModuleViewHolder>(options) {

            @NonNull
            @Override
            public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                System.out.println("created view");
                return new ModuleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ModuleViewHolder moduleViewHolder, int i, @NonNull Module module) {
                System.out.println("binded view");
                if (module.getName() != null && module.getKey() != null) {      // add validation for module key and name to not be null
                    moduleViewHolder.setDetails(module.getName());
                    moduleViewHolder.mView.findViewById(R.id.relative).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, PapersAfterSearch.class);
                            intent.putExtra(MOD_ID, getRef(i).getKey());
                            context.startActivity(intent, ActivityOptions
                                    .makeSceneTransitionAnimation((Activity) context).toBundle());
                        }
                    });
                }
                else
                    moduleViewHolder.mView.setVisibility(View.GONE);
            }
        };
        System.out.println("end");
        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ModuleViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        void setDetails(String name){

            TextView nameView = mView.findViewById(R.id.name_text);
            //TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            //ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            nameView.setText(name);
            //user_status.setText(userStatus);

           // Glide.with(ctx).load(userImage).into(user_image);


        }

    }
}
