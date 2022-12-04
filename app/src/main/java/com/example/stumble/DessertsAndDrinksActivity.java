package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class DessertsAndDrinksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private String listingArray[], descriptionArray[];
    private List<MyDatabase.Listing> listings;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desserts_and_drinks);
        db = new MyDatabase(this);
        //Get selected data of type desserts and inflate the recycler view with the listings
        listings = db.getSelectedData("desserts");

        recyclerView = findViewById(R.id.dessertsRecyclerView);

        MyAdapter myAdapter = new MyAdapter(this, listings);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }
}