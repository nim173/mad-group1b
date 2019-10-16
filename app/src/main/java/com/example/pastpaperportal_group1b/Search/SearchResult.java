package com.example.pastpaperportal_group1b.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pastpaperportal_group1b.PapersAfterSearch;
import com.example.pastpaperportal_group1b.R;
import com.example.pastpaperportal_group1b.ui.main.Module;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Locale;

import static com.example.pastpaperportal_group1b.Search.SearchCommon.MOD_ID;

public class SearchResult extends AppCompatActivity {

    private TableRow tableRow;
    private FirebaseRecyclerAdapter<Module, ModuleViewHolder> firebaseRecyclerAdapter;
    private int itemCount = 0;
    private TextView resultsNos;
    public static final String MOD_NAME = "module name";

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        resultsNos = findViewById(R.id.textView29);
        tableRow = findViewById(R.id.row1NoQ);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Module");
        RecyclerView mResultList = findViewById(R.id.result_list);
        //mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String searchText = intent.getStringExtra(SearchCommon.SEARCH);
        TextView searchTView = findViewById(R.id.textView33);
        searchTView.setText(searchText.trim());

        Query searchQuery = dbRef.orderByChild("name");
        //.startAt(searchText).endAt(searchText + "\uf8ff");
        SnapshotParser<Module> snapshotParser = snapshot -> {
            if (snapshot.getValue(Module.class).getName().toLowerCase().matches(".*" + searchText.trim().toLowerCase() + ".*")) {
                tableRow.setVisibility(View.GONE);
                itemCount++;
                resultsNos.setText(String.format(Locale.getDefault(),"%d ", itemCount));
                return snapshot.getValue(Module.class);
            } else if("".equals(searchText)) {
                tableRow.setVisibility(View.GONE);
                return snapshot.getValue(Module.class);
            }
            else
                return new Module();
        };

        FirebaseRecyclerOptions<Module> options =
                new FirebaseRecyclerOptions.Builder<Module>()
                        .setQuery(searchQuery, snapshotParser)
                        .setLifecycleOwner(this)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Module, ModuleViewHolder>(options) {

            @NonNull
            @Override
            public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ModuleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ModuleViewHolder moduleViewHolder, int i, @NonNull Module module) {
                if (module.getName() != null && module.getKey() != null) {      // add validation for module key and name to not be null
                    moduleViewHolder.setDetails(module.getName(), module.getAbbrev(), module.getKey());
                    moduleViewHolder.mView.findViewById(R.id.relative).setOnClickListener(view -> {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, PapersAfterSearch.class);
                        intent.putExtra(MOD_ID, getRef(i).getKey());
                        TextView name = moduleViewHolder.mView.findViewById(R.id.name_text);
                        intent.putExtra(MOD_NAME, name.getText().toString());
                        context.startActivity(intent);
                    });
                }
                else {
                    moduleViewHolder.mView.setVisibility(View.GONE);
                    moduleViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }
        };
        mResultList.setAdapter(firebaseRecyclerAdapter);
    }

    // View Holder Class

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        View mView;

        ModuleViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setDetails(String name, String abbrev, String key){
            TextView nameView = mView.findViewById(R.id.name_text);
            TextView abbrevKey = mView.findViewById(R.id.abbrevKey);
            nameView.setText(name);
            abbrevKey.setText(String.format("%s - %s", key, abbrev));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,SearchCommon.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    public void goBack(View view) {
        finish();
    }
}
