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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pastpaperportal_group1b.ui.main.PaperUpload;
import com.example.pastpaperportal_group1b.ui.main.PastPaperRV;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;


/*
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
*/

public class ViewPaper extends AppCompatActivity {


    ExpandableRelativeLayout answers; //commented for now
    private DatabaseReference dbRef;
    private String pushId;
    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseRecyclerPagingAdapter<PaperUpload, PastPaperRV> mAdapter;

    public static final String FORUM_KEY = "PASTPAPERPORTAL.FORUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paper);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.yearCard);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);


        Intent intent = getIntent();

        pushId = intent.getStringExtra(UploadOrEdit.ID);


        //Initialize Database
        dbRef = FirebaseDatabase.getInstance().getReference("UploadPaper/PastPaper");

        System.out.println("$$$$$$$$$$$$$$$$$$$$" + UploadOrEdit.ID);
        System.out.println("000000000000000000000000000000000" + pushId);
        System.out.println( pushId = intent.getStringExtra(UploadOrEdit.ID));

        /*dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    TextView PaperId = findViewById(R.id.PaperId);

                    if (dataSnapshot.child("PaperId").getValue() != null) {
                        PaperId.setText(Objects.requireNonNull(dataSnapshot.child("PaperId").getValue()).toString());
                    } else {
                        Toast.makeText(ViewPaper.this, "heeeeeeeeeeey", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });*/

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        DatabasePagingOptions<PaperUpload> options = new DatabasePagingOptions.Builder<PaperUpload>()
                .setLifecycleOwner(this)
                .setQuery(dbRef, config, PaperUpload.class)
                .build();

        mAdapter = new FirebaseRecyclerPagingAdapter<PaperUpload, PastPaperRV>(options) {
            @NonNull
            @Override
            public PastPaperRV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PastPaperRV(LayoutInflater.from(parent.getContext()).inflate(R.layout.paperitem, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PastPaperRV holder,
                                            int position,
                                            @NonNull PaperUpload model) {

                holder.setParameters(model);
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

       /* //Show Dialog to add Items in Database
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*   addPostDialog.show(ViewQuestion.this);*//*
            }
        });*/


    }

   /* public void showAnswers(View view) {

        answers = (ExpandableRelativeLayout) findViewById(R.id.answers);
        answers.toggle();

    }*/
    public void uploadAnswer(View view){
        Intent intentUpload =  new Intent(this, UploadOrEdit.class);
        Button uploadButton = findViewById(R.id.uploadButton);
        startActivity(intentUpload);
    }


    public void showAnswers(View view){
        Intent intent = new Intent(this, AnswersForPapers.class);
        startActivity(intent);
    }

    public void goToForum(View view){
        Intent intentForum = new Intent(this, Forum.class);
        TextView moduleId = findViewById(R.id.moduleId);                  //@Dinuli change textView5 to have the module name as well maybe?
        String heading = moduleId.getText().toString();
        intentForum.putExtra(FORUM_KEY, heading);
        startActivity(intentForum);
    }
}
