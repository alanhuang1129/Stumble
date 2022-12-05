package com.example.stumble;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener, LocationListener {
    private static final String API_KEY = "ZOP5mWhWuiDXiaBGjOUHWXVsFCUTDawJ5JFPxKh1D4eth0w7lgdm_AsV_HCwgjXlDv0bagbxctRQK63Y6BOT1a4jlg7jLS1rw173U0AIl11xc-MRTgVaLCiBrj-FY3Yx";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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

    private double searchLatitude, searchLongitude;

    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventsPageButton = (Button) findViewById(R.id.eventsPageButton);
        eventsPageButton.setOnClickListener(this);
        diningPageButton = (Button) findViewById(R.id.diningPageButton);
        diningPageButton.setOnClickListener(this);
        dessertsPageButton = (Button) findViewById(R.id.dessertsPageButton);
        dessertsPageButton.setOnClickListener(this);
        topPicksPageButton = (Button) findViewById(R.id.topPicksPageButton);
        topPicksPageButton.setOnClickListener(this);
        savedPageButton = (Button) findViewById(R.id.savedPageButton);
        savedPageButton.setOnClickListener(this);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        //To hide bar at the top
        getSupportActionBar().hide();

        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorButton = (Button) findViewById(R.id.sensorButton);
        sensorButton.setOnClickListener(this);

        //seekbar
        distanceSeekBar = (SeekBar) findViewById(R.id.distanceSeekBar);
        distanceSeekBar.setOnSeekBarChangeListener(distanceSeekBarListener);
        distanceSeekBar.setMax(200);
        //Saved Preferences/SQLiteDatabase
        savePreferencesButton = (Button) findViewById(R.id.savePreferencesButton);
        savePreferencesButton.setOnClickListener(this);
        loadPreferencesButton = (Button) findViewById(R.id.loadPreferenceButton);
        loadPreferencesButton.setOnClickListener(this);
        SQLiteButton = (Button) findViewById(R.id.SQLiteButton);
        SQLiteButton.setOnClickListener(this);

        //Create Database
        db = new MyDatabase(this);

        checkConnection();
//        double testLatitude = 49.104431;
//        double testLongitude = -122.801094;
//        searchLatitude = testLatitude;
//        searchLongitude = testLongitude;

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

//        checkLocationPermission();
        getLocation();


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
    protected void onStop() {
        super.onStop();
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
                } else {
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
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                    }
                }
            }
    );

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        switch (sensor.getType()) {
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

    private Location getLocation() {
        Location loc = null;
        try {
            boolean checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.d("checkGPS", checkGPS + "");
//            boolean checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (checkGPS) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
//                       public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                              int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        60000, 10, this);
                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (loc != null) {
                        searchLatitude = loc.getLatitude();
                        searchLongitude = loc.getLongitude();
                        Log.d("location", "Loc: " + searchLatitude + ", " + searchLongitude);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return loc;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String provider = "";
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
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
        int len = 40000000;

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
//                String contentAsString = readIt(is, len);
                String contentAsString = newReadIt(is);
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
    public String newReadIt(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        return buf.toString("UTF-8");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        searchLatitude = location.getLatitude();
        searchLongitude = location.getLongitude();
        Log.d("location", searchLatitude + ", " + searchLongitude);
        new ReadYelpJSONDataTask().execute(
                "https://api.yelp.com/v3/businesses/search?latitude="
                        + searchLatitude + "&longitude=" + searchLongitude
        );
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
                Log.d("testing", "hi");
                JSONArray allBusinesses = new JSONArray(jsonObject.getString("businesses"));
                for (int i = 0; i < allBusinesses.length(); i++) {
                    JSONObject currentInnerObject = allBusinesses.getJSONObject(i);
                    String name = currentInnerObject.getString("name");
                    String type = "dining";
                    String imageURL = currentInnerObject.getString("image_url");
                    boolean isClosed = currentInnerObject.getBoolean("is_closed");
                    double rating = currentInnerObject.getDouble("rating");
                    String price = currentInnerObject.getString("price");
                    String location = currentInnerObject.getString("location");
                    double distance = currentInnerObject.getDouble("distance");
                    long id = db.insertData(name, type, imageURL, isClosed, rating, price, location, distance);
                    if (id < 0) {
                        Log.d("insert", "Inserting error");
                    }
                    else {
                        Log.d("insert", "Insert success");
                    }
                }
            } catch (Exception e) {
                Log.d("ReadYelpJSONDataTask", e.getLocalizedMessage());
            }
        }
    }
}