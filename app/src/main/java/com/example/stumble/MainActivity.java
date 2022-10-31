package com.example.stumble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_EVENTS_ACTIVITY = 0;
    private Button eventsPageButton;
    private Button diningPageButton;
    private Button dessertsPageButton;
    private Button topPicksPageButton;
    private Button savedPageButton;

    private RecyclerView recyclerView;
    private String listingArray[], descriptionArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventsPageButton = (Button)findViewById(R.id.eventsPageButton);
        eventsPageButton.setOnClickListener(this);
        diningPageButton = (Button)findViewById(R.id.diningPageButton);
        diningPageButton.setOnClickListener(this);
        dessertsPageButton = (Button)findViewById(R.id.dessertsPageButton);
        dessertsPageButton.setOnClickListener(this);
        topPicksPageButton = (Button)findViewById(R.id.topPicksPageButton);
        topPicksPageButton.setOnClickListener(this);
        savedPageButton = (Button)findViewById(R.id.savedPageButton);
        savedPageButton.setOnClickListener(this);
        //To hide bar at the top
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.eventsPageButton:
                i = new Intent(this, EventsActivity.class);
                getResult.launch(i);
                break;
            case R.id.diningPageButton:
                i = new Intent(this, DiningActivity.class);
                getResult.launch(i);
                break;
            case R.id.dessertsPageButton:
                i = new Intent(this, DessertsAndDrinksActivity.class);
                getResult.launch(i);
                break;
            case R.id.topPicksPageButton:
                i = new Intent(this, TopPicksActivity.class);
                getResult.launch(i);
                break;
            case R.id.savedPageButton:
                i = new Intent(this, SavedActivity.class);
                getResult.launch(i);
                break;
        }
    }

    ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                    }
                }
            }
    );


}