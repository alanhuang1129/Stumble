package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class TopPicksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private String listingArray[], descriptionArray[];
    private List<MyDatabase.Listing> listings;
    private MyDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_picks);
        db = new MyDatabase(this);
        listings = db.getSelectedData("top");

        recyclerView = findViewById(R.id.topPicksRecyclerView);

//        listingArray = getResources().getStringArray(R.array.listings);
//        descriptionArray = getResources().getStringArray(R.array.listing_descriptions);

        MyAdapter myAdapter = new MyAdapter(this, listings);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }
}