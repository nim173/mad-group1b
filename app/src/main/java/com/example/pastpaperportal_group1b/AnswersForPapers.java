package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.AnswerModel;
import com.example.pastpaperportal_group1b.ui.main.AnswerRV;
import com.example.pastpaperportal_group1b.ui.main.PaperUpload;
import com.example.pastpaperportal_group1b.ui.main.PastPaperRV;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class AnswersForPapers extends AppCompatActivity {
    private DatabaseReference dbRef;
    private String pushId;
    private RecyclerView mRecyclerView;
    FirebaseRecyclerPagingAdapter<AnswerModel, AnswerRV> mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_for_papers);

        //view, add, edit and delete in a dialog

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.ansCard);        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);


        Intent intent = getIntent();

/*        pushId = intent.getStringExtra(UploadOrEdit.ID);*/
        String year = intent.getStringExtra(PapersAfterSearch.VIEW_PAPER);
        pushId = intent.getStringExtra(PapersAfterSearch.PAPER_ID);
        Toast.makeText(this, year + " " + pushId, Toast.LENGTH_SHORT).show();
        String paperName = intent.getStringExtra(ViewPaper.VIEW_NAME);

        //Initialize Database
        dbRef = FirebaseDatabase.getInstance().getReference("Module/" + pushId + '/' + "Years" + "/" + year +  "/" + paperName );
        System.out.println("&&&&&&&&&&&&&&&&&&&&&" + PapersAfterSearch.PAPER_ID);
        System.out.println("$$$$$$$$$$$$$$$$$$$$" + UploadOrEdit.ID);
        System.out.println("000000000000000000000000000000000" + pushId);
        System.out.println( pushId = intent.getStringExtra(UploadOrEdit.ID));
        System.out.println(dbRef + " @@@@@@@@@@@@@@@@@@@@@@@@@@");


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        DatabasePagingOptions<AnswerModel> options = new DatabasePagingOptions.Builder<AnswerModel>()
                .setLifecycleOwner(this)
                .setQuery(dbRef, config, AnswerModel.class)
                .build();

        mAdapter = new FirebaseRecyclerPagingAdapter<AnswerModel, AnswerRV>(options) {
            @NonNull
            @Override
            public AnswerRV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AnswerRV(LayoutInflater.from(parent.getContext()).inflate(R.layout.answeritem, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull AnswerRV holder,
                                            int position,
                                            @NonNull AnswerModel model) {
                    holder.answerName.setText(getRef(position).getKey());
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
}
