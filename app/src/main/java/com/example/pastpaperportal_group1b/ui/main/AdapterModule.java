package com.example.pastpaperportal_group1b.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pastpaperportal_group1b.R;
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
    public void onBindViewHolder(@NonNull MHolder holder, int position) {}

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
