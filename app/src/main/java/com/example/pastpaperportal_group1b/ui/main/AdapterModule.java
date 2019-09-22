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
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterModule extends RecyclerView.Adapter<AdapterModule.MHolder> {

    private Context context;
    List<Module> moduleList;

    //constructor
    public AdapterModule(Context context, List<Module> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout(row.module.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_modules, parent,false);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        // get data
        String module_name = moduleList.get(position).getName();
        String module_abbrev = moduleList.get(position).getAbbrev();
        int module_year = moduleList.get(position).getYear();
        String module_enrol = moduleList.get(position).getKey();
        String module_faculty = moduleList.get(position).getFaculty();

        //set data
        holder.module_name.setText(module_name);
        holder.module_abbrev.setText(module_abbrev);
        holder.module_key.setText(module_enrol);
        holder.module_faculty.setText(module_faculty);
        holder.module_year.setText(module_year);

        holder.module_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display popupmenu
                PopupMenu popupMenu = new PopupMenu(context,holder.module_menu);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_update:
                                Toast.makeText(context,"Changed", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.item_delete:
                                moduleList.remove(position);
                                FirebaseDatabase.getInstance().getReference().child("Module").child(moduleList
                                        .get(position).getPushId()).setValue(null);
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
    }

    @Override
    public int getItemCount() { return moduleList.size(); }

    //view holder class
    class MHolder extends RecyclerView.ViewHolder{

        TextView module_name;
        TextView module_abbrev;
        TextView module_year;
        TextView module_faculty;
        TextView module_key;
        TextView module_menu;

        public MHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            module_name = itemView.findViewById(R.id.module__name);
            module_abbrev = itemView.findViewById(R.id.module__abbrev);
            module_year = itemView.findViewById(R.id.module__year);
            module_faculty = itemView.findViewById(R.id.module__faculty);
            module_key = itemView.findViewById(R.id.module__key);
            module_menu = itemView.findViewById(R.id.module__menu);
        }
    }

}
