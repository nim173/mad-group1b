package com.example.pastpaperportal_group1b.ui.main;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;

public class AnswerRV extends RecyclerView.ViewHolder  {

    public TextView answerName;
    public Button viewPaper;
    public Button downloadPaper;

    public AnswerRV(@NonNull View itemView)
    {
        super(itemView);
        answerName = itemView.findViewById(R.id.answerName);
        viewPaper = itemView.findViewById(R.id.viewAnswer);
        downloadPaper = itemView.findViewById(R.id.downloadAnswer);
    }

    public void setPara(AnswerModel answerModel){

           /* answerName.setText(answerModel.getName());*/
    }
}
