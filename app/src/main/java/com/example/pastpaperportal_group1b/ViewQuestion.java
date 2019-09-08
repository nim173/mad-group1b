package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.PostViewHolder;
import com.example.pastpaperportal_group1b.ui.main.Replies;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ViewQuestion extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseRecyclerPagingAdapter<Replies, PostViewHolder> mAdapter;

    public static final String EDIT = "edit";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String PUSH_ID = "id";
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private static String pushId;
    private AddPostDialog addPostDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        mAuth = FirebaseAuth.getInstance();
        addPostDialog = new AddPostDialog();

        Intent intent = getIntent();
        pushId = intent.getStringExtra(AddQuestionOrAnswer.ID);
        if (pushId != null) {
            //String uid = intent.getStringExtra(AddQuestionOrAnswer.USER);     //for profile picture

            dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question/" + '/' + pushId);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        TextView title = findViewById(R.id.title);
                        TextView body = findViewById(R.id.body);
                        TextView username = findViewById(R.id.username);
                        TextView dateTime = findViewById(R.id.dateTime);
                        Date date = new Date();

                        if (dataSnapshot.child("title").getValue() != null) {
                            title.setText( Objects.requireNonNull( dataSnapshot.child( "title" ).getValue() ).toString());
                            username.setText( Objects.requireNonNull( dataSnapshot.child( "username" ).getValue() ).toString());
                            if ((new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)).equals( Objects.requireNonNull( dataSnapshot.child( "date" ).getValue() ).toString())) {
                                dateTime.setText( Objects.requireNonNull( dataSnapshot.child( "time" ).getValue() ).toString());
                            } else {
                                dateTime.setText( Objects.requireNonNull( dataSnapshot.child( "date" ).getValue() ).toString());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Empty string error", Toast.LENGTH_SHORT).show();
                        }

                        if (dataSnapshot.child("body").getValue() != null) {
                            body.setText( Objects.requireNonNull( dataSnapshot.child( "body" ).getValue() ).toString());
                        } else {
                            body.setText("");
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

            //Initialize RecyclerView
            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            LinearLayoutManager mManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mManager);

            //Initialize Database
            mDatabase = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + pushId);

            //Initialize PagedList Configuration
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPrefetchDistance(5)
                    .setPageSize(5)
                    .build();

            //Initialize FirebasePagingOptions
            DatabasePagingOptions<Replies> options = new DatabasePagingOptions.Builder<Replies>()
                    .setLifecycleOwner(this)
                    .setQuery(mDatabase, config, Replies.class)
                    .build();

            //Initialize Adapter
            mAdapter = new FirebaseRecyclerPagingAdapter<Replies, PostViewHolder>(options) {
                @NonNull
                @Override
                public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
                }

                @Override
                protected void onBindViewHolder(@NonNull PostViewHolder holder,
                                                int position,
                                                @NonNull Replies model) {
                    holder.setItem(model);
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
                    super.onError(databaseError);
                    mSwipeRefreshLayout.setRefreshing(false);
                    databaseError.toException().printStackTrace();
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

            //Show Dialog to add Items in Database
            findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPostDialog.show(ViewQuestion.this);
                }
            });
        }
    }

    public void submit(View view){
        /*
        final EditText taskEditText = new EditText(this);
        taskEditText.setMinLines(3);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a reply")
                .setView(taskEditText)
                .setPositiveButton("Add", (dialog1, which) -> {
                    FirebaseUser currentUser = mAuth.getCurrentUser();        // These will be added after login integration
                    if (currentUser == null) {
                        Toast.makeText(getApplicationContext(), "Please sign in", Toast.LENGTH_SHORT).show();
                    } else {
                        String uid = currentUser.getUid();
                        Date date = new Date();
                        dbRef = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + pushId);
                        Replies rp = new Replies();
                        rp.setBody(taskEditText.getText().toString());
                        rp.setUid(uid);
                        rp.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date));
                        rp.setTime(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
                        dbRef.push().setValue(rp);
                        Toast.makeText(getApplicationContext(), "Reply added successfully", Toast.LENGTH_SHORT).show();
                    }
                } )
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
        */
        addPostDialog.show(ViewQuestion.this);
    }

    public void onDelete(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setView(null)
                .setPositiveButton("Delete", (dialog1, which) -> {
                    dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question").child( pushId );
                    dbRef.removeValue();
                    Toast.makeText( getApplicationContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent intent = new Intent(this, Forum.class);
                    startActivity(intent);
                } )
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void onEdit(View view) {
        Intent intent = new Intent(this, AddQuestionOrAnswer.class );
        TextView title = findViewById( R.id.title);
        TextView body = findViewById( R.id.body);
        intent.putExtra( EDIT, "true");
        intent.putExtra( TITLE, title.getText() );
        intent.putExtra( BODY,  body.getText());
        intent.putExtra( PUSH_ID, pushId );
        startActivity( intent );
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

    //Dialog to add new posts
    class AddPostDialog{

        public void show(Context mContext){
            final Dialog mDialog = new Dialog(mContext);
            mDialog.setContentView(R.layout.dialog_add_layout);
            //final EditText mTitleEditText = mDialog.findViewById(R.id.editTextTitle);
            final EditText mBodyEditText = mDialog.findViewById(R.id.editTextBody);
            Button mAddPostButton = mDialog.findViewById(R.id.buttonAddPost);
            Button mExitButton = mDialog.findViewById(R.id.buttonExit);

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + '/' + pushId);

            mAddPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();        // These will be added after login integration
                    if (currentUser == null) {
                        Toast.makeText(getApplicationContext(), "Please sign in", Toast.LENGTH_SHORT).show();
                    }
                    //String title = mTitleEditText.getText().toString();
                    String body = mBodyEditText.getText().toString();

                    Replies post = new Replies();
                    post.setBody(body);
                    //post.setDate(title);

                    mDatabase.push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mContext, "Reply added", Toast.LENGTH_SHORT).show();
                            mAdapter.refresh();
                            mDialog.dismiss();
                        }
                    });
                }
            });

            mExitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            //Finally, Show the dialog
            mDialog.show();
        }


    }

    /*public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener();
        popup.inflate(R.menu.menu_example);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                return true;
            default:
                return false;
        }
    }*/
}