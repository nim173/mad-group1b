package com.example.pastpaperportal_group1b;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.ui.main.Messages;

import java.util.List;

public class RecyclerView_Config {

    private Context mContext;
    private MessageAdapter mMessageAdapter;

    public void setConfig(RecyclerView recyclerView,Context context,List<Messages> messages,List<String> keys){
        mContext = context;
        mMessageAdapter = new MessageAdapter(messages,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mMessageAdapter);
    }

    class MessageItemView extends RecyclerView.ViewHolder {

        private TextView mSubject;
        private TextView mBody;
        private TextView muserId;
        private TextView mauthor;

        private String key;

        public MessageItemView(ViewGroup parent) {

            super(LayoutInflater.from(mContext).inflate(R.layout.message_list_item1, parent, false));

            mSubject = (TextView) itemView.findViewById(R.id.subject_textView);
            mauthor = (TextView) itemView.findViewById(R.id.author_textView);
        }
        public void Bind(Messages message,String key){
            mSubject.setText(message.getSubject());
            mauthor.setText(message.getAuthor());
            this.key = key;
        }
    }
    class MessageAdapter extends RecyclerView.Adapter<MessageItemView> {
        private List<Messages> mMessageList;
        private List<String> mKeys;

        public MessageAdapter(List<Messages> mMessageList, List<String> mKeys) {
            this.mMessageList = mMessageList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public MessageItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MessageItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageItemView holder, int position) {
            holder.Bind(mMessageList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }
    }

}
