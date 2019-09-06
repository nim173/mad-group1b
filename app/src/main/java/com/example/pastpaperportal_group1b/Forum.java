package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pastpaperportal_group1b.ui.main.PaginationListener;
import com.example.pastpaperportal_group1b.ui.main.PostRecyclerAdapter;
import com.example.pastpaperportal_group1b.ui.main.Question;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static com.example.pastpaperportal_group1b.ui.main.PaginationListener.PAGE_SIZE;
import static com.example.pastpaperportal_group1b.ui.main.PaginationListener.PAGE_START;

public class Forum extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private DatabaseReference dbRef;
    private String lastID;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter adapter;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private int totalPage = 5;
    private boolean isLoading = false;
    int itemCount = 0;

    //good practice to use the key as capital letters since the data received
    // at the next activity will be eventually treated as immutable
    public static final String HEADER_KEY = "PASTPAPERPORTAL.FORUM.QUESTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        String heading = intent.getStringExtra(ViewPaper.FORUM_KEY);
        TextView textView = findViewById(R.id.forum_header);
        textView.setText(heading);

        ButterKnife.bind(this);

        swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new PostRecyclerAdapter(new ArrayList<Question>());
        mRecyclerView.setAdapter(adapter);
        doFirstApiCall();

        /**
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
        startActivity(intentQuestion);
    }

    public void viewQuestion(View view){
        Intent intentViewQ =  new Intent(this, ViewQuestion.class);
        intentViewQ.putExtra(AddQuestionOrAnswer.ID, view.getTag().toString());
        intentViewQ.putExtra(AddQuestionOrAnswer.USER, "GLjlxJJ9QwbcKsk4iZzYpbFNOaP2");
        startActivity(intentViewQ);
    }

    private void doFirstApiCall() {
        final ArrayList<Question> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question");
                dbRef
                        .limitToFirst(PAGE_SIZE+1)
                        .orderByKey()
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    Question postItem = new Question();
                                    postItem.setTitle(postSnapshot.getValue(Question.class).getTitle());
                                    postItem.setUsername(postSnapshot.getValue(Question.class).getUsername());
                                    postItem.setDate(postSnapshot.getValue(Question.class).getDate());
                                    postItem.setPushId(postSnapshot.getKey());
                                    lastID = postSnapshot.getKey();
                                    items.add(postItem);
                                }
                                if (currentPage != PAGE_START) adapter.removeLoading();
                                adapter.addItems(items);
                                swipeRefresh.setRefreshing(false);
                                // check whether is last page or not
                                if (currentPage < totalPage) {
                                    adapter.addLoading();
                                } else {
                                    isLastPage = true;
                                }
                                isLoading = false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }, 0);
    }

    private void doApiCall() {
        final ArrayList<Question> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question");
                dbRef
                        .limitToFirst(PAGE_SIZE+1)
                        .orderByKey()
                        .startAt(lastID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    //items.add(postSnapshot.getValue(Question.class));
                                    Question postItem = new Question();
                                    postItem.setTitle(postSnapshot.getValue(Question.class).getTitle());
                                    postItem.setUsername(postSnapshot.getValue(Question.class).getUsername());
                                    postItem.setDate(postSnapshot.getValue(Question.class).getDate());
                                    postItem.setPushId(postSnapshot.getKey());
                                    lastID = postSnapshot.getKey();
                                    items.add(postItem);
                                }
                                if (currentPage != PAGE_START) adapter.removeLoading();
                                items.remove(0);
                                adapter.addItems(items);
                                swipeRefresh.setRefreshing(false);
                                //isLastPage = true;
                                // check whether is last page or not
                                if (currentPage < totalPage) {
                                    adapter.addLoading();
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

                items.add(postItem);
*/
                /*
                 * manage progress view
                 */

            }
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
}

