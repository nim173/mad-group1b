package com.example.pastpaperportal_group1b.ui.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView textViewTitle;
    TextView textViewBody;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewBody = itemView.findViewById(R.id.textViewBody);
    }

    public void setItem(Replies reply){
        textViewTitle.setText(reply.getBody());
        textViewBody.setText(reply.getDate());
    }
}
