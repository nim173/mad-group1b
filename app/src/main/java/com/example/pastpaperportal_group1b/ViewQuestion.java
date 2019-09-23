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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pastpaperportal_group1b.ui.main.PostViewHolder;
import com.example.pastpaperportal_group1b.ui.main.Replies;
import com.google.android.material.snackbar.Snackbar;
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
import static com.example.pastpaperportal_group1b.AddQuestionOrAnswer.FROM_ADD;
import static com.example.pastpaperportal_group1b.AddQuestionOrAnswer.FROM_EDIT;

public class ViewQuestion extends AppCompatActivity {

    public static final String FROM_DELETE = "from_delete";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseRecyclerPagingAdapter<Replies, PostViewHolder> mAdapter;

    public static final String EDIT = "edit";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String PUSH_ID = "id";
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private static String pushId;
    private AddPostDialog addPostDialog;
    private TableRow noA;
    private int itemCount = 0;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        mAuth = FirebaseAuth.getInstance();
        addPostDialog = new AddPostDialog();
        noA = findViewById(R.id.row1NoQ);
        TextView noView = findViewById(R.id.textView16);

        Intent intent = getIntent();
        if ("true".equals(intent.getStringExtra(FROM_ADD)))
            Snackbar.make(findViewById(android.R.id.content), "Question successfully added!!", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(0, 184, 212)).show();
        if ("true".equals(intent.getStringExtra(FROM_EDIT)))
            Snackbar.make(findViewById(android.R.id.content), "Question edited successfully", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(0, 184, 212)).show();
        pushId = intent.getStringExtra(AddQuestionOrAnswer.ID);
        path = intent.getStringExtra(Forum.PATH);
        if (pushId != null) {
            dbRef = FirebaseDatabase.getInstance().getReference("Forum/Question/"+ path + '/' + pushId);
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
            RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            LinearLayoutManager mManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mManager);

            //Initialize Database
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + path + '/' + pushId);

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
                    return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
                }

                @Override
                protected void onBindViewHolder(@NonNull PostViewHolder holder,
                                                int position,
                                                @NonNull Replies model) {
                    holder.setItem(model);
                    holder.textViewBody.setOnClickListener(view -> {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(ViewQuestion.this, holder.textViewBody, Gravity.END);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.menu_example);
                        //adding click listener
                        popup.setOnMenuItemClickListener(item -> {
                                if (item.getItemId() == (R.id.delete)) {
                                    dbRef = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + path + '/' + pushId + "/" + model.getPushId());
                                    dbRef.removeValue();
                                    mAdapter.refresh();
                                    Snackbar.make(findViewById(android.R.id.content), "Item deleted successfully", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(0, 184, 212)).show();
                                    return true;
                                }
                                else
                                    return false;
                        });
                        //displaying the popup
                        popup.show();

                    });
                }

                @Override
                protected void onLoadingStateChanged(@NonNull LoadingState state) {
                    switch (state) {
                        case LOADING_INITIAL:
                        case LOADING_MORE:
                            // Do your loading animation
                            noA.setVisibility(View.GONE);
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
                            //retry();
                            mSwipeRefreshLayout.setRefreshing(false);
                            findViewById(R.id.row1NoQ).setVisibility(View.VISIBLE);
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
            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                mAdapter.refresh();
                noA.setVisibility(View.GONE);
            });

            //Show Dialog to add Items in Database
            findViewById(R.id.fab_add).setOnClickListener(v -> addPostDialog.show(ViewQuestion.this));

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    noView.setText(String.format(Locale.getDefault(),"%d", dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void submit(View view){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            signInSnackBar();
        } else {
            addPostDialog.show(ViewQuestion.this);
        }
    }

    private void signInSnackBar(){
        Snackbar.make(findViewById(android.R.id.content), "Please sign in", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(255, 174, 66))
                .setAction("Sign In", v1 -> {
                    Context context = v1.getContext();
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }).setActionTextColor(Color.rgb(0,0,0)).show();
    }

    public void onDelete(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            signInSnackBar();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setView(null)
                    .setPositiveButton("Delete", (dialog1, which) -> {
                        DatabaseReference dbRefd = FirebaseDatabase.getInstance().getReference("Forum/Question/" + path + "/" + pushId);
                        dbRefd.removeValue();
                        dbRef = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + path + '/' + pushId);
                        dbRef.removeValue();
                        //Intent intent = new Intent(this, Forum.class);
                        //intent.putExtra(FROM_DELETE, "true");
                        //startActivity(intent);
                        Snackbar.make(findViewById(android.R.id.content), "Item deleted successfully", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(0, 184, 212)).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }
    }

    public void onEdit(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            signInSnackBar();
        } else {
            Intent intent = new Intent(this, AddQuestionOrAnswer.class);
            TextView title = findViewById(R.id.title);
            TextView body = findViewById(R.id.body);
            intent.putExtra(EDIT, "true");
            intent.putExtra(TITLE, title.getText());
            intent.putExtra(BODY, body.getText());
            intent.putExtra(PUSH_ID, pushId);
            intent.putExtra(Forum.PATH, path);
            startActivity(intent);
        }
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

            void show(Context mContext){
            final Dialog mDialog = new Dialog(mContext);
            mDialog.setContentView(R.layout.dialog_add_layout);
            final EditText mBodyEditText = mDialog.findViewById(R.id.editTextBody);
            Button mAddPostButton = mDialog.findViewById(R.id.buttonAddPost);
            Button mExitButton = mDialog.findViewById(R.id.buttonExit);

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Forum/Replies/" + path + '/' + pushId);

            mAddPostButton.setOnClickListener(v -> {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    signInSnackBar();
                } else if (TextUtils.isEmpty(mBodyEditText.getText().toString().trim())){
                    Snackbar.make(findViewById(android.R.id.content), "Reply cannot be empty!!", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(179,58,58)).show();
                } else {
                    Replies post = new Replies();
                    post.setBody(mBodyEditText.getText().toString());
                    post.setUsername(currentUser.getDisplayName());
                    post.setUid(currentUser.getUid());
                    Uri uri = currentUser.getPhotoUrl();
                    if(!(uri == null)) {
                        post.setPhotoUrl(Objects.requireNonNull(uri.toString()));
                        post.setPhotoUrl(Objects.requireNonNull(currentUser.getPhotoUrl()).toString());
                    }
                    Date date = new Date();
                    post.setDate( new SimpleDateFormat( "dd-MM-yyyy", Locale.getDefault() ).format( date ) );
                    post.setTime( new SimpleDateFormat( "HH:mm", Locale.getDefault() ).format( date ) );

                    DatabaseReference newRef = mDatabase.push();
                    String pushId = newRef.getKey();
                    post.setPushId(pushId);
                    newRef.setValue( post );

                    newRef.setValue(post).addOnSuccessListener(aVoid -> {
                        Snackbar.make(findViewById(android.R.id.content), "Reply successfully added!!", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(0, 184, 212)).show();
                        mAdapter.refresh();
                        mDialog.dismiss();
                    });
                }
            });
            mExitButton.setOnClickListener(v -> mDialog.dismiss());
            //Finally, Show the dialog
            mDialog.show();
        }
    }
}