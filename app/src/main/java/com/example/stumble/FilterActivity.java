package com.example.stumble;

import static com.example.stumble.MainActivity.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class FilterActivity extends AppCompatActivity  {

    private SharedPreferences sharedPrefs;
    private SeekBar distanceSeekBar;
    private TextView distanceTextView;
    private EditText searchFilter;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        db = new MyDatabase(this);

        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        searchFilter = (EditText) findViewById(R.id.filterEditText);
        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //seekbar
        distanceSeekBar = (SeekBar) findViewById(R.id.distanceSeekBar);
        distanceSeekBar.setOnSeekBarChangeListener(distanceSeekBarListener);
        distanceSeekBar.setMax(40000);

        searchFilter.setText(sharedPrefs.getString("Filter", ""));
        distanceSeekBar.setProgress(sharedPrefs.getInt("Distance", 0));
    }

    @Override
    protected void onStop() {
        sharedPrefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("Distance", distanceSeekBar.getProgress());
        editor.putString("Filter", searchFilter.getText().toString());
        editor.commit();
        db.deleteRowByType("dining");
        super.onStop();
    }

    private SeekBar.OnSeekBarChangeListener distanceSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            distanceTextView.setText("Distance (" + progress + "m)");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}