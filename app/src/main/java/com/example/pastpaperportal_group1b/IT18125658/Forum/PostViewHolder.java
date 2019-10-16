package com.example.pastpaperportal_group1b.IT18125658.Forum;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView textViewBody;
    private TextView textViewUsername;
    private TextView textViewDate;
    private TextView textViewPush;
    private ImageView imageView;

    PostViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewBody = itemView.findViewById(R.id.title);
        textViewUsername = itemView.findViewById(R.id.username);
        textViewDate = itemView.findViewById(R.id.date);
        textViewPush = itemView.findViewById(R.id.push);
        imageView = itemView.findViewById(R.id.dp1);
    }

    public void setItem(Replies reply){
        textViewBody.setText(reply.getBody());
        textViewUsername.setText(reply.getUsername());
        textViewPush.setText(reply.getPushId());
        Picasso.get().load(reply.getPhotoUrl()).into(imageView);
        Date date = new Date();
        if ((new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)).equals(reply.getDate())) {
            textViewDate.setText(reply.getTime());
        } else {
            textViewDate.setText(reply.getDate());
        }

    }
}
