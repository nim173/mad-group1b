package com.example.pastpaperportal_group1b.ui.main;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;

public class YearRv extends RecyclerView.ViewHolder {

        public TextView year;
        Button view;


    public YearRv(@NonNull View itemView) {
        super(itemView);
        year = itemView.findViewById(R.id.year);
        view = itemView.findViewById(R.id.view);
    }

        public void setValue (YearModel module){
        year.setText(module.getName());
    }
}

