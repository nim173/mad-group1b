package com.example.pastpaperportal_group1b.ui.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;

public class PastPaperRV extends RecyclerView.ViewHolder  {

    TextView viewName;
    /*Button downloadButton;
    Button addAnswer;
    Button viewDetails;*/

    public PastPaperRV(@NonNull View itemView) {
        super(itemView);
        viewName = itemView.findViewById(R.id.PaperId);
        /*downloadButton = itemView.findViewById(R.id.downloadButton);
        viewDetails = itemView.findViewById(R.id. view);
        addAnswer = itemView.findViewById(R.id.AddAnswers);*/
    }

    public void setParameters(PaperUpload paperUpload){
        viewName.setText(paperUpload.getPaperId());
    }
}
