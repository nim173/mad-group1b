package com.example.pastpaperportal_group1b;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pastpaperportal_group1b.ui.main.PaperUpload;
import com.example.pastpaperportal_group1b.ui.main.PastPaperRV;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class ViewPaper extends AppCompatActivity {


    ExpandableRelativeLayout answers; //commented for now
    private DatabaseReference dbRef;
    private String pushId;
    private RecyclerView mRecyclerView;
    private String name;
    public static final String VIEW_NAME = "answer";
    public static final String MODULE_ID = "module_id";
    public static final String YEAR = "year";
    private String year;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseRecyclerPagingAdapter<PaperUpload, PastPaperRV> mAdapter;

    public static final String FORUM_KEY = "PASTPAPERPORTAL.FORUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paper);

        Intent intent = getIntent();
        year = intent.getStringExtra(PapersAfterSearch.VIEW_PAPER);
        pushId = intent.getStringExtra(PapersAfterSearch.PAPER_ID);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + year +" " +pushId);
        Toast.makeText(this, year + " " + pushId, Toast.LENGTH_SHORT).show();
        TextView name = findViewById(R.id.PaperId);
 /*       name.setText(Paperi);*/

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.yearCard);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        //Initialize Database
        dbRef = FirebaseDatabase.getInstance().getReference("Module" + '/' + pushId + "/Years" + '/' + year);

        /*System.out.println("$$$$$$$$$$$$$$$$$$$$" + UploadOrEdit.ID);
        System.out.println("00000000000000000000" + pushId);
        System.out.println( pushId = intent.getStringExtra(UploadOrEdit.ID));*/
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
                System.out.println("modellllllllllllllllllllllllllllllll " + model.getPaperId() + model.getModuleId() );
                //model.setPaperId(getRef(position).getKey());
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
        intentForum.putExtra(FORUM_KEY, heading);
        startActivity(intentForum);
    }

    //IMPLEMENT LISTENER for download to work

    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }
}
