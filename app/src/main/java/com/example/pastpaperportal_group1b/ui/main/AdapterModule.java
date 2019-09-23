package com.example.pastpaperportal_group1b.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pastpaperportal_group1b.R;
import com.example.pastpaperportal_group1b.add_new_module;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
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
        String module_pid = moduleList.get(position).getPushId();

        //set data
        holder.module_name.setText(module_name);
        holder.module_abbrev.setText(module_abbrev);
        holder.module_key.setText(module_enrol);
        holder.module_faculty.setText(module_faculty);
        holder.module_year.setText("Year "+module_year);

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
                                editmodule(position);
                                break;
                            case R.id.item_delete:
                                System.out.println(module_pid);
                                FirebaseDatabase.getInstance().getReference().child("Module").child(module_pid).setValue(null);
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

    public void editmodule(int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View xx = inflater.inflate(R.layout.module_layout_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Module");
        builder.setView(xx);

        EditText mname = (EditText) xx.findViewById(R.id.name_module);
        EditText mabbrev = (EditText) xx.findViewById(R.id.abbrev_module);
        EditText mkey = (EditText) xx.findViewById(R.id.key_module);
        Spinner mfaculty = (Spinner) xx.findViewById(R.id.faculty_spinner);
        Spinner myear = (Spinner) xx.findViewById(R.id.year_spinner);



        mname.setText(moduleList.get(position).getName());
        mabbrev.setText(moduleList.get(position).getAbbrev());
        mkey.setText(moduleList.get(position).getKey());
        mfaculty.setTag(moduleList.get(position).getFaculty());
        myear.setTag(moduleList.get(position).getYear());

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{ dialog.wait(); }catch (Exception e) {}
                DatabaseReference databaseModules = FirebaseDatabase.getInstance().getReference("Module");

                if(!mkey.getText().toString().equals(mkey.getText().toString())){
                Query getdata = databaseModules.orderByChild("key").equalTo(mkey.getText().toString().trim());
                getdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Toast.makeText(context,"Enrollment Key Exists",Toast.LENGTH_LONG).show();
                        }
                        else {
                            HashMap<String,Object> result = new HashMap<>();
                            result.put("name", mname.getText().toString().trim());
                            result.put("abbrev", mabbrev.getText().toString().trim());
                            result.put("key", mkey.getText().toString().trim());
                            result.put("year", Integer.parseInt(myear.getSelectedItem().toString().trim()));
                            result.put("faculty", mfaculty.getSelectedItem().toString().trim());
                            System.out.println(moduleList.get(position).getPushId());
                            FirebaseDatabase.getInstance().getReference().child("Messages")
                                    .child(moduleList.get(position).getPushId())
                                    .updateChildren(result);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                }else {
                    HashMap<String,Object> result = new HashMap<>();
                    result.put("name", mname.getText().toString().trim());
                    result.put("abbrev", mabbrev.getText().toString().trim());
                    result.put("key", mkey.getText().toString().trim());
                    result.put("year", Integer.parseInt(myear.getSelectedItem().toString().trim()));
                    result.put("faculty", mfaculty.getSelectedItem().toString().trim());
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Module");
                    db.child(moduleList.get(position).getPushId()).updateChildren(result);
                }
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });builder.create().show();
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
