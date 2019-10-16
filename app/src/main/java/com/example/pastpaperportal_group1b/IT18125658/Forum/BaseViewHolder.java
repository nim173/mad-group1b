package com.example.pastpaperportal_group1b.IT18125658.Forum;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int mCurrentPosition;

    BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        setCurrentPosition(position);
        clear();
    }

    private void setCurrentPosition(int position) {
        this.mCurrentPosition = position;
    }
}
