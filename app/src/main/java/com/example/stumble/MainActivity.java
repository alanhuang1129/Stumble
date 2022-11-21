package com.example.stumble;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private static final int REQUEST_EVENTS_ACTIVITY = 0;
    private Button eventsPageButton;
    private Button diningPageButton;
    private Button dessertsPageButton;
    private Button topPicksPageButton;
    private Button savedPageButton;

    private Button sensorButton;
    private SensorManager mySensorManager;
    private boolean sensorButtonOn = false;
    private boolean detectShake = false;

    private RecyclerView recyclerView;
    private String listingArray[], descriptionArray[];

    private MyDatabase db;
    private SeekBar distanceSeekBar;
    private TextView distanceTextView;
    private Button savePreferencesButton, loadPreferencesButton;

    private Button SQLiteButton;

    private SharedPreferences sharedPrefs;
    public static final int DEFAULT = 100;

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
        distanceTextView = (TextView)findViewById(R.id.distanceTextView);
        //To hide bar at the top
        getSupportActionBar().hide();

        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorButton = (Button)findViewById(R.id.sensorButton);
        sensorButton.setOnClickListener(this);

        distanceSeekBar = (SeekBar) findViewById(R.id.distanceSeekBar);
        distanceSeekBar.setOnSeekBarChangeListener(distanceSeekBarListener);
        distanceSeekBar.setMax(200);
//        int seekBarPreference = sharedPrefs.getInt("Distance", DEFAULT);
//        distanceSeekBar.setProgress(seekBarPreference);
        savePreferencesButton = (Button)findViewById(R.id.savePreferencesButton);
        savePreferencesButton.setOnClickListener(this);
        loadPreferencesButton = (Button)findViewById(R.id.loadPreferenceButton);
        loadPreferencesButton.setOnClickListener(this);
        SQLiteButton = (Button)findViewById(R.id.SQLiteButton);
        SQLiteButton.setOnClickListener(this);

        //Create Database
        db = new MyDatabase(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this,
                mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mySensorManager.unregisterListener(this);
        super.onPause();
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
            case R.id.sensorButton:
                sensorButtonOn = !sensorButtonOn;
                detectShake = !detectShake;
                if (sensorButtonOn) {
                    sensorButton.setText("Shake phone to receive random event");
                }
                else {
                    sensorButton.setText("Random Event");
                }
                break;
            case R.id.savePreferencesButton:
                sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("Distance", distanceSeekBar.getProgress());
                editor.commit();
                break;
            case R.id.loadPreferenceButton:
                int seekBarPreference = sharedPrefs.getInt("Distance", DEFAULT);
                distanceSeekBar.setProgress(seekBarPreference);
                break;
            case R.id.SQLiteButton:
                i = new Intent(this, SQLiteActivity.class);
                getResult.launch(i);
                break;
        }
    }

    private SeekBar.OnSeekBarChangeListener distanceSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            distanceTextView.setText("Distance (" + progress + "km)");
//            sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            editor.putInt("Distance", progress);
//            editor.commit();
//            Log.d("seekbar", sharedPrefs.getInt("Distance", DEFAULT) + "");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        switch(sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (Math.abs(sensorEvent.values[1]) > 10.4 && detectShake) {
                    Intent i = new Intent(this, SelectedActivity.class);
                    getResult.launch(i);
                }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}