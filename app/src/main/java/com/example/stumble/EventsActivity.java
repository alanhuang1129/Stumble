package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private String listingArray[], descriptionArray[];
    private List<MyDatabase.Listing> listings;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        db = new MyDatabase(this);
        listings = db.getSelectedData("event");

        recyclerView = findViewById(R.id.eventsRecyclerView);

//        listingArray = getResources().getStringArray(R.array.listings);
//        descriptionArray = getResources().getStringArray(R.array.listing_descriptions);

//        MyAdapter myAdapter = new MyAdapter(this, listingArray, descriptionArray);
        MyAdapter myAdapter = new MyAdapter(this, listings);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();

        checkConnection();
//        new ReadYelpJSONDataTask().execute(
//                "http://api.geonames.org/findNearByWeatherJSON?lat=" +
//                        txtLat.getEditableText().toString() + "&lng=" +
//                        txtLong.getText().toString() + "&username=free");
    }



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
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("tag", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

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
                return readJSONData(urls[0]);
            }catch(IOException e){
                exception = e;
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {
//                URL url = new URL ("https://reqres.in/api/users");
//                HttpURLConnection con = (HttpURLConnection)url.openConnection();

//                con.setRequestMethod("POST");
//                con.setRequestProperty("Content-Type", "application/json");
//                con.setRequestProperty("Accept", "application/json");
//                con.setDoOutput(true);
//                String jsonInputString = "{"name": "Upendra", "job": "Programmer"}";

//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject weatherObservationItems =
//                        new JSONObject(jsonObject.getString("weatherObservation"));
//
//                txtTemp.setText("TEMPERATURE: " + weatherObservationItems.getString("temperature"));
//                txtWeatherDisplay.setText("FULL INFO: " + weatherObservationItems.toString());
            } catch (Exception e) {
                Log.d("ReadWeatherJSONDataTask", e.getLocalizedMessage());
            }
        }
    }

    private class ReadLocJSONDataTask extends AsyncTask<String, Void, String> {

        Exception exception = null;

        protected String doInBackground(String... urls) {
            try{
                return readJSONData(urls[0]);
            }catch(IOException e){
                exception = e;
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray locObservationItems =
                        new JSONArray(jsonObject.getString("geonames"));
                JSONObject innerObject = (JSONObject)locObservationItems.getJSONObject(0);
//                Toast.makeText(this, "location", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.d("ReadLocJSONDataTask", e.getLocalizedMessage());
            }
        }
    }
}