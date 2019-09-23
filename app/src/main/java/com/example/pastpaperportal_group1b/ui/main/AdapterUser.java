package com.example.pastpaperportal_group1b.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pastpaperportal_group1b.R;
import com.example.pastpaperportal_group1b.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder> {

    private Context context;
    List<User> userList;

    //constructot
    public AdapterUser(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout(row.user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        // get data
        String user_name = userList.get(position).getName();
        String user_email = userList.get(position).getEmail();
        String user_image = userList.get(position).getUrl();
        String user_id = userList.get(position).getUid();

        //set data
        holder.muser_name.setText(user_name);
        holder.muser_email.setText(user_email);
        try {
            Picasso.get().load(user_image)
                    .placeholder(R.drawable.ic_default_img)
                    .into(holder.muser_avatar);
        }
        catch (Exception e ){

        }
        //handle option menu
        holder.mtxtOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display popupmenu
                PopupMenu popupMenu = new PopupMenu(context,holder.mtxtOptionDigit);
                popupMenu.inflate(R.menu.option_menu1);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_item_remove:
                                userList.remove(position);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).setValue(null);
                                notifyDataSetChanged();
                                Toast.makeText(context,"Removed",Toast.LENGTH_SHORT).show();
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
        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+user_email, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView muser_avatar;
        TextView muser_name;
        TextView muser_email;
        TextView mtxtOptionDigit;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            muser_avatar = itemView.findViewById(R.id.user_avatar);
            muser_name = itemView.findViewById(R.id.user_name);
            muser_email = itemView.findViewById(R.id.user_email);
            mtxtOptionDigit = itemView.findViewById(R.id.txtOptionDigit);
        }
    }
}
