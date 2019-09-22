package com.example.pastpaperportal_group1b.ui.main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;

public class AnswerRV extends RecyclerView.ViewHolder  {

    public TextView answerName;
    public TextView desc;
    public Button viewPaper;
    public ImageButton downloadPaper;
    public ImageButton options;

    public AnswerRV(@NonNull View itemView)
    {
        super(itemView);
        answerName = itemView.findViewById(R.id.answerName);
        desc = itemView.findViewById(R.id.description);
        downloadPaper = itemView.findViewById(R.id.downloadAnswer);
        options = itemView.findViewById(R.id.options);
    }

    public void setPara(AnswerModel answerModel){

           /* answerName.setText(answerModel.getName());*/
    }
}
