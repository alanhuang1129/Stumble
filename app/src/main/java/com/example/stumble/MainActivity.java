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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private static final String API_KEY = "ZOP5mWhWuiDXiaBGjOUHWXVsFCUTDawJ5JFPxKh1D4eth0w7lgdm_AsV_HCwgjXlDv0bagbxctRQK63Y6BOT1a4jlg7jLS1rw173U0AIl11xc-MRTgVaLCiBrj-FY3Yx";

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

    private MyDatabase db;
    private SeekBar distanceSeekBar;
    private TextView distanceTextView;
    private Button savePreferencesButton, loadPreferencesButton;

    private Button SQLiteButton;

    private SharedPreferences sharedPrefs;
    public static final int DEFAULT = 100;

    private float searchLatitude, searchLongitude;

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

        //seekbar
        distanceSeekBar = (SeekBar) findViewById(R.id.distanceSeekBar);
        distanceSeekBar.setOnSeekBarChangeListener(distanceSeekBarListener);
        distanceSeekBar.setMax(200);
        //Saved Preferences/SQLiteDatabase
        savePreferencesButton = (Button)findViewById(R.id.savePreferencesButton);
        savePreferencesButton.setOnClickListener(this);
        loadPreferencesButton = (Button)findViewById(R.id.loadPreferenceButton);
        loadPreferencesButton.setOnClickListener(this);
        SQLiteButton = (Button)findViewById(R.id.SQLiteButton);
        SQLiteButton.setOnClickListener(this);

        //Create Database
        db = new MyDatabase(this);

        checkConnection();

        new ReadYelpJSONDataTask().execute(
                "https://api.yelp.com/v3/businesses/search?latitude="
                + searchLatitude + "&longitude=" + searchLongitude
        );
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
        //List of buttons
        switch (v.getId()) {
            case R.id.eventsPageButton:
                //Move to events activity
                i = new Intent(this, EventsActivity.class);
                getResult.launch(i);
                break;
            case R.id.diningPageButton:
                //Move to dining activity
                i = new Intent(this, DiningActivity.class);
                getResult.launch(i);
                break;
            case R.id.dessertsPageButton:
                //Move to desserts/drinks activity
                i = new Intent(this, DessertsAndDrinksActivity.class);
                getResult.launch(i);
                break;
            case R.id.topPicksPageButton:
                //Move to top picks activity
                i = new Intent(this, TopPicksActivity.class);
                getResult.launch(i);
                break;
            case R.id.savedPageButton:
                //Move to saved listings activity
                i = new Intent(this, SavedActivity.class);
                getResult.launch(i);
                break;
            case R.id.sensorButton:
                //On/off switch for sensor button and detectShake booleans
                sensorButtonOn = !sensorButtonOn;
                detectShake = !detectShake;
                if (sensorButtonOn) {
                    //If on, set the text (The sensor will start detecting when it is on)
                    sensorButton.setText("Shake phone to receive random event");
                }
                else {
                    //Button is off
                    sensorButton.setText("Random Event");
                }
                break;
            case R.id.savePreferencesButton:
                //Save the preferences
                sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("Distance", distanceSeekBar.getProgress());
                editor.commit();
                break;
            case R.id.loadPreferenceButton:
                //Load the preferences
                int seekBarPreference = sharedPrefs.getInt("Distance", DEFAULT);
                distanceSeekBar.setProgress(seekBarPreference);
                break;
            case R.id.SQLiteButton:
                //Head to SQLite Database Activity (This activity is purely for demo
                //Will remove this activity later because an API will insert the data instead
                i = new Intent(this, SQLiteActivity.class);
                getResult.launch(i);
                break;
        }
    }

    private SeekBar.OnSeekBarChangeListener distanceSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            distanceTextView.setText("Distance (" + progress + "km)");
            //Tried to implement shared preferences within the seekbar
            //Did not work as intended...
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
    //Method for explicit intents
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
                //If the phone is shaken downwards while the sensor is detecting
                //Move to "Random" Activity (Not random yet)
                if (Math.abs(sensorEvent.values[1]) > 10.4 && detectShake) {
                    Intent i = new Intent(this, SelectedActivity.class);
                    getResult.launch(i);
                }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //Everything below is not implemented yet and will be moved to main activity
    //This is for getting the API to work
    public void checkConnection(){
        ConnectivityManager connectMgr =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //fetch data

            //display network information in a TextView
            //String networkInformation = networkInfo.toString();
            Log.d("debug", "connection ok");
        }
        else {
            //display error
            Log.d("debug", "network error");
        }
    }

    private String readJSONData(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 2500;

        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {

            conn.setReadTimeout(30000 /* milliseconds */);
            conn.setConnectTimeout(45000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
//            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("response2", response +"");
            if (response == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                Log.d("result", contentAsString);
                return contentAsString;
            }
            else {
                Log.d("response", "Response Code: " + response);
                return null;
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private class ReadYelpJSONDataTask extends AsyncTask<String, Void, String> {

        Exception exception = null;

        protected String doInBackground(String... urls) {
            try{
                Log.d("url", urls[0]);
                return readJSONData(urls[0]);
            }catch(IOException e){
                Log.e("exception", e +"");
                exception = e;
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {
                Log.d("result", result);
                JSONObject jsonObject = new JSONObject(result);

            } catch (Exception e) {
                Log.d("ReadYelpJSONDataTask", e.getLocalizedMessage());
            }
        }
    }
}