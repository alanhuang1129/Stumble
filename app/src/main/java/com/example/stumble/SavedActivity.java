package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private String listingArray[], descriptionArray[];
    private List<MyDatabase.Listing> listings;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        db = new MyDatabase(this);
        //Get selected data of type saved and inflate the recycler view with the listings
        listings = db.getSelectedSavedData("saved");

        recyclerView = findViewById(R.id.savedRecyclerView);

        MyAdapter myAdapter = new MyAdapter(this, listings);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }
}