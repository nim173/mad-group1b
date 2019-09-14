package com.example.pastpaperportal_group1b;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.ui.main.FirebaseDatabaseHelper;
import com.example.pastpaperportal_group1b.ui.main.Messages;
import com.squareup.picasso.Picasso;

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
        private TextView mauthor;
        private TextView mdate;
        private TextView option_menu;
        private ImageView imageView;

        private String key;


        public MessageItemView(ViewGroup parent) {

            super(LayoutInflater.from(mContext).inflate(R.layout.message_list_item1, parent, false));

            mSubject = (TextView) itemView.findViewById(R.id.subject_textView);
            mauthor = (TextView) itemView.findViewById(R.id.author_textView);
            mdate = (TextView) itemView.findViewById(R.id.date);
            option_menu = (TextView) itemView.findViewById(R.id.option_menu1);
            //mBody = (TextView) itemView.findViewById(R.id.subject_textView);
            imageView = (ImageView) itemView.findViewById(R.id.dp1);
        }
        public void Bind(Messages message,String key){
            mSubject.setText(message.getSubject());
            mauthor.setText(message.getAuthor());
            mdate.setText(message.getDate());
            Picasso.get().load(message.getPhotoUrl()).into(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
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
                                    updatemsgDialog(holder,position);
                                    break;
                                case R.id.item_delete:
                                    deletemsgDialog(holder,position);
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
        builder.setMessage(msg.getBody());

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void updatemsgDialog (MessageItemView holder, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View xx = inflater.inflate(R.layout.msg_layout_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Edit the message");
        builder.setView(xx);

        EditText ssubject = (EditText) xx.findViewById(R.id.ssubject);
        EditText bbody = (EditText) xx.findViewById(R.id.bbody);
        String mauthor = holder.mauthor.getText().toString();
        String mdate = holder.mdate.getText().toString();
        String mImage = holder.imageView.toString();

        ssubject.setText(holder.mSubject.getText());
        bbody.setText("");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Messages messages = new Messages();
                messages.setSubject(ssubject.getText().toString());
                messages.setBody(bbody.getText().toString());
                messages.setAuthor(mauthor);
                messages.setDate(mdate);
                new FirebaseDatabaseHelper().updateMessage(holder.key, messages, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Messages> Messages, List<String> Keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(mContext,"Message Updated Successfully",Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }

    private  void deletemsgDialog(MessageItemView holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Are you sure want to delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new FirebaseDatabaseHelper().deleteMessage(holder.key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Messages> Messages, List<String> Keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(mContext,"Deleted Successfully",Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

}
