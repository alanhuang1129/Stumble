package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class TopPicksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyDatabase.Listing> listings;
    private MyDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_picks);
        db = new MyDatabase(this);
        //Get selected data of type top and inflate the recycler view with the listings
        listings = db.getSelectedTopPicksData("top");

        recyclerView = findViewById(R.id.topPicksRecyclerView);

        MyAdapter myAdapter = new MyAdapter(this, listings);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }

    @Override
    protected void onStop() {
        db.deleteTopPicksRowByType("top");
        super.onStop();
    }
}