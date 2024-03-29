package com.example.pastpaperportal_group1b.IT18125658.Forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private List<Question> mPostItems;

    PostRecyclerAdapter(List<Question> postItems) {
        this.mPostItems = postItems;
    }

    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                throw new IllegalArgumentException("Invalid View type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }

    void addItems(List<Question> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    void addLoading(Question question) {
        isLoaderVisible = true;
        mPostItems.add(question);
        notifyItemInserted(mPostItems.size() - 1);
    }

    void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        Question item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    private Question getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.title)
        TextView textViewTitle;
        @BindView(R.id.username)
        TextView textViewUsername;
        @BindView(R.id.date)
        TextView textViewDate;
//        @BindView(R.id.card_view)
//        CardView cardView;
        @BindView((R.id.dp1))
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            Question item = mPostItems.get(position);
            textViewTitle.setText(item.getTitle());
            textViewUsername.setText(item.getUsername());
            Date date = new Date();
            if ((new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)).equals(item.getDate())) {
                textViewDate.setText(item.getTime());
            } else {
                textViewDate.setText(item.getDate());
            }
            textViewTitle.setTag(item.getPushId());
            Picasso.get().load(item.getPhotoUrl()).into(imageView);
        }
    }

    protected class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }
}
