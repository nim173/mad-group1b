package com.example.pastpaperportal_group1b.ui.main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.Image;
import com.example.pastpaperportal_group1b.R;

public class PastPaperRV extends RecyclerView.ViewHolder  {

    TextView viewName;
    ImageButton ans;
    public ImageButton downloadPaper;
    public ImageButton options;

    public PastPaperRV(@NonNull View itemView) {
        super(itemView);
        viewName = itemView.findViewById(R.id.PaperId);
        ans = itemView.findViewById(R.id.AddAnswers);
        downloadPaper = itemView.findViewById(R.id.downloadButton);
        options = itemView.findViewById(R.id.options);
    }

    public void setParameters(PaperUpload paperUpload){
        viewName.setText(paperUpload.getPaperId());
        ans.setTag(paperUpload.getPaperId());
    }
}
