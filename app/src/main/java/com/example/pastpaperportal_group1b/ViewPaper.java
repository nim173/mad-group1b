package com.example.pastpaperportal_group1b;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pastpaperportal_group1b.ui.main.PaperUpload;
import com.example.pastpaperportal_group1b.ui.main.PastPaperRV;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class ViewPaper extends AppCompatActivity {

    private String pushId;
    private RecyclerView mRecyclerView;
    private String name;
    public static final String VIEW_NAME = "answer";
    public static final String MODULE_ID = "module_id";
    public static final String YEAR = "year";
    private String year;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String modName;
    public Dialog dialog;
    private FirebaseAuth mAuth;

    private DatabaseReference dbRef;

    FirebaseRecyclerPagingAdapter<PaperUpload, PastPaperRV> mAdapter;

    public static final String FORUM_KEY = "PASTPAPERPORTAL.FORUM";
    private ImageButton downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paper);
        Intent intent = getIntent();
        year = intent.getStringExtra(PapersAfterSearch.VIEW_PAPER);
        pushId = intent.getStringExtra(PapersAfterSearch.PAPER_ID);
        modName = intent.getStringExtra(SearchResult.MOD_NAME);
        System.out.println("!!!!!!!!!!!!!!!!!" + year +" " +pushId);
   /*     Toast.makeText(this, year + " " + pushId, Toast.LENGTH_SHORT).show();*/
        TextView name = findViewById(R.id.moduleId);
       name.setText(modName);

        downloadButton = findViewById(R.id.downloadButton);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.yearCard);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        //Initialize Database
        dbRef = FirebaseDatabase.getInstance().getReference("Module" + '/' + pushId + "/Years" + '/' + year);

        System.out.println(" ^^^^ ^^^^ ^^^^ ^^^^ ^^^^ ^^^^ ^^^^ "+dbRef);

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

                holder.downloadPaper.setOnClickListener(view -> {
                    //downloadFile(model.getUrl());
                    Uri webpage = Uri.parse(model.getUrl());
                    System.out.println("000000000000000000000000000000000000000000000 " + model.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        }
                });

                holder.options.setOnClickListener(view -> {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(ViewPaper.this, holder.options, Gravity.END);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.deletepaper);
                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        dbRef = FirebaseDatabase.getInstance().getReference("Module/" + pushId + '/' + "Years" + "/" + year + "/"+ getRef(position).getKey());
                        if (item.getItemId() == (R.id.delete)) {
                            dbRef.removeValue();
                            mAdapter.refresh();
                            Snackbar.make(findViewById(android.R.id.content), "Item deleted successfully",
                                    Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                                    .setBackgroundTint(Color.rgb(0, 184, 212)).show();
                            return true;
                        } else {
                            return false;
                        }});
                    //displaying the popup
                    popup.show();

                });

                System.out.println("modelllllllllllllllll " + model.getPaperId() + model.getModuleId() );
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
    }


    private void signInSnackBar(){
        Snackbar.make(findViewById(android.R.id.content), "Please sign in", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(255, 174, 66))
                .setAction("Sign In", v1 -> {
                    Context context = v1.getContext();
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }).setActionTextColor(Color.rgb(0,0,0)).show();
    }


    public void showAnswers(View view){
        Intent intent = new Intent(this, AnswersForPapers.class);
        intent.putExtra(VIEW_NAME,view.getTag().toString() );
        intent.putExtra(MODULE_ID, pushId);
        System.out.println("showanswerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+name+ " " +pushId);
        intent.putExtra(YEAR, year);
        startActivity(intent);
    }

    public void goToForum(View view){
        Intent intentForum = new Intent(this, Forum.class);
        TextView moduleId = findViewById(R.id.moduleId);                  //@Dinuli change textView5 to have the module name as well maybe?
        String heading = moduleId.getText().toString();
        intentForum.putExtra(FORUM_KEY, modName + " - " + year);
        intentForum.putExtra(YEAR, year);
        intentForum.putExtra(MODULE_ID, pushId);

        startActivity(intentForum);
    }

}


