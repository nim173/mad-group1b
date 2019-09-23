package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pastpaperportal_group1b.ui.main.PaginationListener;
import com.example.pastpaperportal_group1b.ui.main.PostRecyclerAdapter;
import com.example.pastpaperportal_group1b.ui.main.Question;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.pastpaperportal_group1b.ViewQuestion.EDIT;
import static com.example.pastpaperportal_group1b.ViewQuestion.FROM_DELETE;
import static com.example.pastpaperportal_group1b.ui.main.PaginationListener.PAGE_SIZE;
import static com.example.pastpaperportal_group1b.ui.main.PaginationListener.PAGE_START;

public class Forum extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String PATH = "path for storing";
    private DatabaseReference dbRef;
    private String lastID;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter adapter;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private final int totalPage = 10;
    private boolean isLoading = false;
    private int itemCount = 0;
    private Question question;
    private String moduleId;
    private String year;
    //good practice to use the key as capital letters since the data received
    // at the next activity will be eventually treated as immutable
    public static final String HEADER_KEY = "PASTPAPERPORTAL.FORUM.QUESTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        String heading = intent.getStringExtra(ViewPaper.FORUM_KEY);
        moduleId = intent.getStringExtra(ViewPaper.MODULE_ID);
        year = intent.getStringExtra(ViewPaper.YEAR);
        if ("true".equals(intent.getStringExtra(FROM_DELETE)))
            Snackbar.make(findViewById(android.R.id.content), "Item deleted successfully", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(0, 184, 212)).show();
        TextView textView = findViewById(R.id.forum_header);
        textView.setText(heading);

        ButterKnife.bind(this);
        question = new Question();
        swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new PostRecyclerAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(adapter);
        adapter.addLoading(question);
        FirebaseDatabase.getInstance().getReference("Forum/Question/" + moduleId + "/" + year).orderByChild("revDate").limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() == 0){
                            adapter.removeLoading();
                            findViewById(R.id.row1NoQ).setVisibility(View.VISIBLE);
                        }else{
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                findViewById(R.id.row1NoQ).setVisibility(View.GONE);
                                lastID = postSnapshot.getKey();
                                question.setTitle( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getTitle());
                                question.setUsername( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getUsername());
                                question.setDate( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getDate());
                                question.setUid(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getUid());
                                question.setPhotoUrl(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getPhotoUrl());
                                question.setTime(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getTime());
                                question.setPushId(postSnapshot.getKey());
                            }
                            doFirstApiCall();
                        }}

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "No data to fetch", Toast.LENGTH_SHORT).show();
                    }
                });


        /*swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new PostRecyclerAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(adapter);
        doFirstApiCall();
        adapter.addLoading(question);*/
        /*
         * add scroll listener while user reach in bottom load more will call
         */
        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                doApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    public void addQuestion(View view){
        Intent intentQuestion = new Intent(this, AddQuestionOrAnswer.class);
        TextView header = findViewById(R.id.forum_header);                  //@Dinuli change textView5 to have the module name as well maybe?
        String heading = header.getText().toString();
        intentQuestion.putExtra(HEADER_KEY, heading);
        intentQuestion.putExtra( EDIT, "false");
        intentQuestion.putExtra(PATH, moduleId + "/" + year);
        startActivity(intentQuestion);
        finish();
    }

    public void viewQuestion(View view){
        Intent intentViewQ =  new Intent(this, ViewQuestion.class);
        intentViewQ.putExtra(AddQuestionOrAnswer.ID, view.getTag().toString());
        intentViewQ.putExtra(PATH, moduleId + "/" + year);
        startActivity(intentViewQ);
        finish();
    }

    private void doFirstApiCall() {
        findViewById(R.id.row1NoQ).setVisibility(View.GONE);
        final ArrayList<Question> items = new ArrayList<>();
        new Handler().postDelayed( () -> {
            dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question/" + moduleId + "/" + year);
            dbRef.orderByChild("revDate")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getChildrenCount() == 0){
                                swipeRefresh.setRefreshing(false);
                                findViewById(R.id.row1NoQ).setVisibility(View.VISIBLE);
                            }else{
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    lastID = postSnapshot.getKey();
                                    Question postItem = new Question();
                                    postItem.setTitle( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getTitle());
                                    postItem.setUsername( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getUsername());
                                    postItem.setDate( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getDate());
                                    postItem.setUid(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getUid());
                                    postItem.setPhotoUrl(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getPhotoUrl());
                                    postItem.setTime(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getTime());
                                    postItem.setPushId(postSnapshot.getKey());
                                    items.add(postItem);
                                }
                                //adapter.removeLoading();
                                if (currentPage != PAGE_START) adapter.removeLoading();
                                items.remove(0);
                                adapter.addItems(items);
                                swipeRefresh.setRefreshing(false);
                                // check whether is last page or not
                                if (currentPage < totalPage) {
                                    adapter.addLoading(question);
                                } else {
                                    isLastPage = true;
                                }
                                isLoading = false;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }, 0);
    }

    private void doApiCall() {
        findViewById(R.id.row1NoQ).setVisibility(View.GONE);
        final ArrayList<Question> items = new ArrayList<>();
        new Handler().postDelayed( () -> {
            dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question/" + moduleId + "/" + year);
            dbRef.limitToFirst(PAGE_SIZE+1)
                    .orderByChild("revDate")
                    .startAt(lastID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                //items.add(postSnapshot.getValue(Question.class));
                                Question postItem = new Question();
                                postItem.setTitle( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getTitle());
                                postItem.setUsername( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getUsername());
                                postItem.setDate( Objects.requireNonNull( postSnapshot.getValue( Question.class ) ).getDate());
                                postItem.setUid(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getUid());
                                postItem.setPhotoUrl(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getPhotoUrl());
                                postItem.setTime(Objects.requireNonNull(postSnapshot.getValue(Question.class)).getTime());
                                postItem.setPushId(postSnapshot.getKey());
                                lastID = postSnapshot.getKey();
                                items.add(postItem);
                            }
                            if (currentPage != PAGE_START) adapter.removeLoading();
//                            items.remove(0);
                            adapter.addItems(items);
                            swipeRefresh.setRefreshing(false);
                            // check whether is last page or not
                            if (currentPage < totalPage) {
                                adapter.addLoading(question);
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            /*for(Question q: items){
                System.out.println("############*****"+q.getTitle());
            }*/

           /* for (int i = 0; i < 4; i++) {
                itemCount++;
                Question postItem = new Question();
                postItem.setTitle("Question1" + itemCount);
                postItem.setUsername("User123");
                postItem.setDate("27-09-2019");
                items.add(postItem);
            }
*/ /*
            Question postItem = new Question();
            postItem.setTitle("Question1" + itemCount);
            postItem.setUsername("User123");
            postItem.setDate("27-09-2019");
            items.add(postItem);
            items.add(postItem);
            items.add(postItem);
            items.add(postItem);
            items.add(postItem);*/
            /*
             * manage progress view
             */

        }, 0);
    }
    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        doFirstApiCall();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        doFirstApiCall();
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}