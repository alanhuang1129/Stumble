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
import android.widget.EditText;
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
    public static final String SHARED_PREFS = "sharedPrefs";

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
    private Button filterSettingsButton;

    private SharedPreferences sharedPrefs;
    public static final int DEFAULT = 10000;

    private double searchLatitude, searchLongitude;
    private String filterCategory;

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
        filterSettingsButton = (Button) findViewById(R.id.filterSettingsButton);
        filterSettingsButton.setOnClickListener(this);
        //To hide bar at the top
        getSupportActionBar().hide();

        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorButton = (Button) findViewById(R.id.sensorButton);
        sensorButton.setOnClickListener(this);

        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        //Create Database
        db = new MyDatabase(this);

        checkConnection();

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

//        checkLocationPermission();
        getLocation();


    }
    //API search every onStart()
    @Override
    protected void onStart() {
        super.onStart();
        filterCategory = sharedPrefs.getString("Filter", "");
        int seekBarProgress = sharedPrefs.getInt("Distance", 0);
        new ReadYelpJSONDataTask().execute(
                "https://api.yelp.com/v3/businesses/search?latitude="
                        + searchLatitude + "&longitude=" + searchLongitude
                        + "&radius=" + seekBarProgress + "&categories=" + filterCategory
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        //List of button selections
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
            case R.id.filterSettingsButton:
                i = new Intent(this, FilterActivity.class);
                getResult.launch(i);
                break;
        }
    }


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
                if (Math.abs(sensorEvent.values[1]) > 11.0 && detectShake) {
                    Intent i = new Intent(this, SelectedActivity.class);
                    MyDatabase.Listing randomListing = db.getRandomListing();
                    if (randomListing != null) {
                        i.putExtra("Name", randomListing.lName);
                        i.putExtra("Type", randomListing.lType);
                        i.putExtra("Image", randomListing.lImageUrl);
                        i.putExtra("IsClosed", randomListing.lIsClosed);
                        i.putExtra("Rating", randomListing.lRating);
                        i.putExtra("Price", randomListing.lPrice);
                        i.putExtra("Location", randomListing.lLocation);
                        i.putExtra("Latitude", randomListing.lLatitude);
                        i.putExtra("Longitude", randomListing.lLongitude);
                        i.putExtra("Distance", randomListing.lDistance);
                        getResult.launch(i);
                    }
                }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //Receive GPS location to feed information into the Yelp API
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

    //Check Location Method, I don't think it's being used right now
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
            //Authentication
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

    //InputStream Handling Methods
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
                //Process the JSON
                Log.d("result", result);
                JSONObject jsonObject = new JSONObject(result);
                Log.d("testing", "hi");
                JSONArray allBusinesses = new JSONArray(jsonObject.getString("businesses"));
                //For handling the JSONArray, since the Array length is dynamic
                for (int i = 0; i < allBusinesses.length(); i++) {
                    JSONObject currentInnerObject = allBusinesses.getJSONObject(i);
                    JSONObject coordinateObject = currentInnerObject.getJSONObject("coordinates");
                    String name = currentInnerObject.getString("name");
                    String type = "dining";
                    String imageURL = currentInnerObject.getString("image_url");
                    boolean isClosed = currentInnerObject.getBoolean("is_closed");
                    double rating = currentInnerObject.getDouble("rating");
                    String price = currentInnerObject.getString("price");
                    String location = currentInnerObject.getString("location");
                    double latitude = coordinateObject.getDouble("latitude");
                    double longitude = coordinateObject.getDouble("longitude");
                    double distance = currentInnerObject.getDouble("distance");
                    //If the listing is not in the database, add it
                    long id = db.insertData(name, type, imageURL, isClosed, rating, price, location, latitude, longitude, distance);
                    if (id < 0) {
                        Log.d("insert", "Inserting error");
                    } else {
                        Log.d("insert", "Insert success");
                    }

                    /*** ADDING THIS PART IN BREAKS SOME FUNCTIONALITY
                     * It was to insert into the top picks database for the top picks activity
                     * Criteria is for a high review rating of 4.0 or higher***/
//                    If rating is over 4.0, add into top picks
//                    if (rating >= 4.0) {
//                        long id2 = db.insertTopPicksData(name, "top", imageURL, isClosed, rating, price, location, latitude, longitude, distance);
//                        if (id2 < 0) {
//                            Log.d("insert", "Inserting top picks error");
//                        } else {
//                            Log.d("insert", "Insert top picks success");
//                        }
//                    }
                }
            } catch (Exception e) {
                Log.d("ReadYelpJSONDataTask", e.getLocalizedMessage());
            }
        }
    }
}