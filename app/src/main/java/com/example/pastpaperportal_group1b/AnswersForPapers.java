package com.example.pastpaperportal_group1b;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.ValueIterator;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.pastpaperportal_group1b.ui.main.AnswerModel;
import com.example.pastpaperportal_group1b.ui.main.AnswerRV;
import com.example.pastpaperportal_group1b.ui.main.PaperUpload;
import com.example.pastpaperportal_group1b.ui.main.PastPaperRV;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import java.io.File;
import java.io.IOException;

public class AnswersForPapers extends AppCompatActivity {
    private DatabaseReference dbRef;
    private String pushId;
    private RecyclerView mRecyclerView;
    private addAnswer addAnswer;
    private FirebaseRecyclerPagingAdapter<AnswerModel, AnswerRV> mAdapter;
    StorageReference storageRef;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public Dialog dialog;
    private FirebaseAuth mAuth;
    public Uri url;
    private String year;    private String paperName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_for_papers);

        mAuth = FirebaseAuth.getInstance();
        addAnswer = new addAnswer();
        //view, add, edit and delete in a dialog
        storageRef = FirebaseStorage.getInstance().getReference();

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.ansCard);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        Intent intent = getIntent();

/*        pushId = intent.getStringExtra(UploadOrEdit.ID);*/
        year = intent.getStringExtra(ViewPaper.YEAR);
        pushId = intent.getStringExtra(ViewPaper.MODULE_ID);
        Toast.makeText(this, year + " " + pushId, Toast.LENGTH_SHORT).show();
        paperName = intent.getStringExtra(ViewPaper.VIEW_NAME);


        //Initialize Database
        dbRef = FirebaseDatabase.getInstance().getReference("Module/" + pushId + '/' + "Years" + "/" + year +  "/" + paperName + "/Answers" );
        System.out.println("000000000000000000000000000000000" + pushId);
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
                    holder.answerName.setText(model.getName());
                    holder.desc.setText(model.getDesc());

                holder.options.setOnClickListener(view -> {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(AnswersForPapers.this, holder.options, Gravity.END);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options);
                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        dbRef = FirebaseDatabase.getInstance().getReference("Module/" + pushId + '/' + "Years" + "/" + year + "/" + paperName + "/Answers" + "/" + getRef(position).getKey());
                        if (item.getItemId() == (R.id.delete)) {
                                    dbRef.removeValue();
                                    mAdapter.refresh();
                                    Snackbar.make(findViewById(android.R.id.content), "Item deleted successfully",
                                            Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                                            .setBackgroundTint(Color.rgb(0, 184, 212)).show();
                                    return true;
                                } else if (item.getItemId() == (R.id.edit)) {
                                    edit(model.getName(), model.getDesc());
                                } else {
                                    return false;
                                }return false;
                            });
                    //displaying the popup
                    popup.show();

                });

                holder.downloadPaper.setOnClickListener( view -> {

                        //downloadFile(model.getUrl());
                            Uri webpage = Uri.parse(model.getUrl());
                            System.out.println("000000000000000000000000000000000000000000000 " + model.getUrl());
                            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                });

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

    private void downloadFile(String url) throws IOException {
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);

        System.out.println("_____________________________________________________" + reference);
        File localFile = File.createTempFile("application", "pdf");

        reference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            // Local temp file has been created
            Toast.makeText(AnswersForPapers.this, "Downloaded", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception -> {
            // Handle any errors
            System.out.println("error bro");

        });
    }


    class addAnswer {
        void display(Context context){

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.add_answer_dialog);
            final EditText name = dialog.findViewById(R.id.nameText);
            final EditText desc = dialog.findViewById(R.id.noteText);
            Button uploadButton = dialog.findViewById(R.id.selectButton);
            Button submit = dialog.findViewById(R.id.submitButton);
            Button cancel = dialog.findViewById(R.id.exitButton);

            final  DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            submit.setOnClickListener(view -> {

            /*    FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    signInSnackBar();
                } else if (TextUtils.isEmpty(mBodyEditText.getText().toString().trim())){
                    Snackbar.make(findViewById(android.R.id.content), "Reply cannot be empty!!", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(179,58,58)).show();
                } else {*/

                AnswerModel answerModel = new AnswerModel();

                answerModel.setName(name.getText().toString().trim());
               /* answerModel.setUsername(currentUser.getDisplayName());*/
                answerModel.setDesc(desc.getText().toString().trim());
                answerModel.setUrl(url.toString());
                //dbRef.child(dbRef.push().getKey()).setValue(pdfName.getText().toString(), url.toString());
                DatabaseReference newRef = dbRef.push();
                // dbRef.push().setValue(answerModel);
                newRef.setValue( answerModel );
                Toast.makeText(AnswersForPapers.this, "Uploaded", Toast.LENGTH_SHORT).show();
            });

            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("______________________________________________________ENTERED1");

                    selectFile();
                }
            });

            cancel.setOnClickListener(v -> dialog.dismiss());

            //Finally, Show the dialog
            dialog.show();

        }

    }


       public void edit(String answerName, String des) {

           FirebaseUser currentUser = mAuth.getCurrentUser();
           if (currentUser == null) {
               signInSnackBar();
           } else {
               dialog = new Dialog(AnswersForPapers.this);
               dialog.setContentView(R.layout.edit_dialog);
               final EditText name = dialog.findViewById(R.id.nameText);
               final EditText desc = dialog.findViewById(R.id.descText);
               name.setText(answerName);
               desc.setText(des);
               Button submit = dialog.findViewById(R.id.submitEdit);
               Button cancel = dialog.findViewById(R.id.cancel);

               final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

               submit.setOnClickListener(view1 -> {

            /*    FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    signInSnackBar();
                } else if (TextUtils.isEmpty(mBodyEditText.getText().toString().trim())){
                    Snackbar.make(findViewById(android.R.id.content), "Reply cannot be empty!!", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.rgb(179,58,58)).show();
                } else {*/

                /*AnswerModel answerModel = new AnswerModel();

                answerModel.setName(name.getText().toString().trim());
                answerModel.setDesc(desc.getText().toString().trim());*/
/*                DatabaseReference newRef = dbRef.push();
                newRef.setValue( answerModel );*/
                   dbRef.child("name").setValue(name.getText().toString().trim());
                   dbRef.child("desc").setValue(desc.getText().toString().trim());
                   Toast.makeText(AnswersForPapers.this, "Uploaded", Toast.LENGTH_SHORT).show();
               });

               cancel.setOnClickListener(v -> dialog.dismiss());

               //Finally, Show the dialog
               dialog.show();

           }
       }

    private void selectFile() {
            System.out.println("______________________________________________________ENTERED2");
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT); //FETCH FILES
            //startActivityForResult(Intent.createChooser(intent, "Select a PDF"), 1);
            startActivityForResult(Intent.createChooser(intent, "Select PDF file"), 1);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            System.out.println("______________________________________________________ENTERED3");
            if(requestCode ==1 && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
                System.out.println("______________________________________________________ENTERED4");
                uploadFile(data.getData());
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


    private void uploadFile(Uri data) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            signInSnackBar();
        } else {
            System.out.println("__________________________________________________ENTERED5");
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference reference = storageRef.child("Answers" + System.currentTimeMillis() + ".pdf");
            reference.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            url = uri.getResult();


                            // AnswerModel answerModel;
                            // answerModel = new AnswerModel(pdfName.getText().toString(), url.toString());
                            // EditText pdfName = dialog.findViewById(R.id.noteText);

                            progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded: " + (int) progress + "%");
                }
            });
        }

    }
    /* public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }

    }*/
    public void add(View view){

        addAnswer.display(AnswersForPapers.this);

    }


}
