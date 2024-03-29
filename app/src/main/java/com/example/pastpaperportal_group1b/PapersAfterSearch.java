package com.example.pastpaperportal_group1b;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pastpaperportal_group1b.IT18125658.Search.SearchCommon;
import com.example.pastpaperportal_group1b.IT18125658.Search.SearchResult;
import com.example.pastpaperportal_group1b.ui.main.YearModel;
import com.example.pastpaperportal_group1b.ui.main.YearRv;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import java.util.Objects;

public class PapersAfterSearch extends AppCompatActivity {

    public static final String VIEW_PAPER = "view";
    public static final String PAPER_ID = "paper_id";
    private String pushId;
    private String modName;
    FirebaseRecyclerPagingAdapter<YearModel, YearRv> mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers_after_search);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.yearCard);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);


        Intent intent = getIntent();
        pushId = intent.getStringExtra(SearchCommon.MOD_ID);
        modName = intent.getStringExtra(SearchResult.MOD_NAME);

        TextView moduleId = findViewById(R.id.moduleId);
        moduleId.setText(modName);

        //Initialize Database
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Module" + '/' + pushId + '/' + "Years");
        System.out.println(dbRef);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(6)
                .setPageSize(10)
                .build();

        DatabasePagingOptions<YearModel> options = new DatabasePagingOptions.Builder<YearModel>()
                .setLifecycleOwner(this)
                .setQuery(dbRef, config, YearModel.class)
                .build();

        mAdapter = new FirebaseRecyclerPagingAdapter<YearModel, YearRv>(options) {
            @NonNull
            @Override
            public YearRv onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new YearRv(LayoutInflater.from(parent.getContext()).inflate(R.layout.yearitem, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull YearRv holder,
                                            int position,
                                            @NonNull YearModel  model) {
                model.setName(getRef(position).getKey());
                holder.setValue(model);

                //listener
                holder.viewButton.setOnClickListener(view -> {
                    Context context = view.getContext();
                    Intent intent1 = new Intent(context, ViewPaper.class);
                    intent1.putExtra(VIEW_PAPER, getRef(position).getKey());
                    intent1.putExtra(PAPER_ID, pushId);
                    intent1.putExtra(SearchResult.MOD_NAME, modName);
                    context.startActivity(intent1);
                }

                );
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:

                    case FINISHED:
                        //Reached end of Data set
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
                // Handle Error

            }
        };

        //Set Adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        //Set listener to SwipeRefreshLayout for refresh action
        mSwipeRefreshLayout.setOnRefreshListener(() -> mAdapter.refresh());
        ActionBar actionBar=getSupportActionBar();
        //actionBar.setTitle("Manage Users");
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//go to previous activity
        return super.onSupportNavigateUp();
    }

    public void addPaper(View view) {
        Intent intent = new Intent(this, UploadOrEdit.class);
        intent.putExtra(PAPER_ID, pushId);
        startActivity(intent);
    }

    //Start Listening Adapter
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
