package com.example.firebasedemo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    MainAdapter mainAdapter;





@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    FirebaseRecyclerOptions<MainModel> options =
            new FirebaseRecyclerOptions.Builder<MainModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("search"),MainModel.class)
                    .build();

    mainAdapter = new MainAdapter(options);
    recyclerView.setAdapter(mainAdapter);
}

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_item, menu);
    MenuItem item = menu.findItem(R.id.searchId);
    SearchView searchView = (SearchView) item.getActionView();

    assert searchView != null;
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String newText) {
            mysearch(newText);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            mysearch(newText);
            return false;
        }
    });
    return super.onCreateOptionsMenu(menu);
}

    private void mysearch (String newText) {

    FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("search").orderByChild("email").startAt(newText).endAt(newText + "\uf8ff"),MainModel.class)
                        .build();

    mainAdapter = new MainAdapter(options);
    mainAdapter.startListening();
    recyclerView.setAdapter(mainAdapter);
    }


}