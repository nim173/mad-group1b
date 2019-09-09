package com.example.pastpaperportal_group1b;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
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
        private TextView mdate;
        private TextView option_menu;


        private String key;

        public MessageItemView(ViewGroup parent) {

            super(LayoutInflater.from(mContext).inflate(R.layout.message_list_item1, parent, false));

            mSubject = (TextView) itemView.findViewById(R.id.subject_textView);
            mauthor = (TextView) itemView.findViewById(R.id.author_textView);
            mdate = (TextView) itemView.findViewById(R.id.date);
            option_menu = (TextView) itemView.findViewById(R.id.option_menu);
            mBody = (TextView) itemView.findViewById(R.id.subject_textView);
        }
        public void Bind(Messages message,String key){
            mSubject.setText(message.getSubject());
            mauthor.setText(message.getAuthor());
            mdate.setText(message.getDate());
            mBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showmessagedialog(message);
                }
            });
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
            holder.option_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, holder.option_menu);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()) {
                                case R.id.item_update:
                                    Toast.makeText(mContext, "updated",Toast.LENGTH_LONG).show();
                                    break;
                                case R.id.item_delete:
                                    Toast.makeText(mContext, "deleted",Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            holder.Bind(mMessageList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }
    }

    private void showmessagedialog(Messages msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(msg.getSubject());

        LinearLayout linearLayout=new LinearLayout(mContext);
        final TextView subject=new TextView(mContext);
        final TextView body=new TextView(mContext);

        //subject.setText(msg.getSubject());
        body.setText(msg.getBody());

        linearLayout.addView(subject);
        //linearLayout.addView(body);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

}
