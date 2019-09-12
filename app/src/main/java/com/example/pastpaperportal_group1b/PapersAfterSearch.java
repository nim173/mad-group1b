package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pastpaperportal_group1b.ui.main.Module;
import com.example.pastpaperportal_group1b.ui.main.YearModel;
import com.example.pastpaperportal_group1b.ui.main.YearRv;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class PapersAfterSearch extends AppCompatActivity {

    public static final String VIEW_PAPER = "view";
    public static final String PAPER_ID = "paper_id";
    private DatabaseReference dbRef;
    private RecyclerView mRecyclerView;
    FirebaseRecyclerPagingAdapter<YearModel, YearRv> mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers_after_search);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.yearCard);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);


        Intent intent = getIntent();
        String pushId = intent.getStringExtra(SearchCommon.MOD_ID);

        TextView moduleId = findViewById(R.id.moduleId);
        moduleId.setText(pushId);

        System.out.println("***********************************************************" + pushId);

        //Initialize Database
        dbRef = FirebaseDatabase.getInstance().getReference("Module" + '/' + pushId + '/' + "Years" );
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
                holder.year.setText(getRef(position).getKey());
                System.out.println("########################"+model.getUrl());

                //listener
                holder.viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ViewPaper.class);
                        intent.putExtra(VIEW_PAPER, getRef(position).getKey());
                        intent.putExtra(PAPER_ID, pushId);
                        context.startActivity(intent);
                    }
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
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });


    }

    public void addPaper(View view) {
        Intent intent = new Intent(this, UploadOrEdit.class);
        startActivity(intent);
    }

}
