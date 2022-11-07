package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class DessertsAndDrinksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String listingArray[], descriptionArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desserts_and_drinks);

        recyclerView = findViewById(R.id.dessertsRecyclerView);

        listingArray = getResources().getStringArray(R.array.listings);
        descriptionArray = getResources().getStringArray(R.array.listing_descriptions);

        MyAdapter myAdapter = new MyAdapter(this, listingArray, descriptionArray);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }
}