package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

//Activity for containing and displaying the saved listings
public class SavedActivity extends AppCompatActivity implements View.OnClickListener{
    private Button clearSavedButton;
    private RecyclerView recyclerView;
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
        clearSavedButton = (Button) findViewById(R.id.clearSavedButton);
        clearSavedButton.setOnClickListener(this);

        MyAdapter myAdapter = new MyAdapter(this, listings);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearSavedButton:
                db.deleteSavedRowByType("saved");
                Toast.makeText(this, "Listings Cleared", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}