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
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        textView = findViewById(R.id.textView20);
        dbRef = FirebaseDatabase.getInstance().getReference("Module");
        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String searchText = intent.getStringExtra(SearchCommon.SEARCH);
        firebaseUserSearch(searchText);
    }

    private void firebaseUserSearch(String searchText) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx " );
        Query searchQuery = dbRef.orderByChild("name");
                //.startAt(searchText).endAt(searchText + "\uf8ff");

        SnapshotParser<Module> snapshotParser = snapshot -> {
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb " + "".equals(searchText));
            if (snapshot.getValue(Module.class).getName().toLowerCase().matches(".*" + searchText.toLowerCase() + ".*")) {
                textView.setVisibility(View.GONE);
                return snapshot.getValue(Module.class);
            } else if("".equals(searchText)) {
                textView.setVisibility(View.GONE);
                return snapshot.getValue(Module.class);
            }
            else
                return new Module();
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
                return new ModuleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ModuleViewHolder moduleViewHolder, int i, @NonNull Module module) {
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
