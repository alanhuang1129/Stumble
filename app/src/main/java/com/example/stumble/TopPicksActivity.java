package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class TopPicksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String listingArray[], descriptionArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_picks);

        recyclerView = findViewById(R.id.topPicksRecyclerView);

        listingArray = getResources().getStringArray(R.array.listings);
        descriptionArray = getResources().getStringArray(R.array.listing_descriptions);

        MyAdapter myAdapter = new MyAdapter(this, listingArray, descriptionArray);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }
}