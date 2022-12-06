package com.example.stumble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.location.LocationListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;

//Activity responsible for acting as the page with the listing description and details
public class SelectedActivity extends AppCompatActivity implements View.OnClickListener{

    private Button googleMapsButton, saveButton;
    private TextView titleTextView, ratingTextView, isClosedTextView, distanceTextView;
    private ImageView image;
    private WebView locationView;
    private MyDatabase db;

    private String name, type, imageURL, price, location;
    private double rating, latitude, longitude, distance;
    private boolean isClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        //To hide bar at the top
        getSupportActionBar().hide();

        googleMapsButton = (Button) findViewById(R.id.googleMapsButton);
        googleMapsButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.saveListing);
        saveButton.setOnClickListener(this);
        db = new MyDatabase(this);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        ratingTextView = (TextView) findViewById(R.id.ratingTextView);
        isClosedTextView = (TextView) findViewById(R.id.isClosedTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        image = (ImageView) findViewById(R.id.listingImage);
        locationView = (WebView) findViewById(R.id.webView);
        locationView.loadUrl("https://www.google.com/maps/search/" + titleTextView.getText() + "/@" +
                latitude + "," + longitude);

        name = getIntent().getStringExtra("Name");
        type = getIntent().getStringExtra("Type");
        imageURL = getIntent().getStringExtra("Image");
        price = getIntent().getStringExtra("Price");
        location = getIntent().getStringExtra("Location");

        rating = getIntent().getDoubleExtra("Rating", 0);
        latitude = getIntent().getDoubleExtra("Latitude", 0);
        longitude = getIntent().getDoubleExtra("Longitude", 0);
        distance = getIntent().getDoubleExtra("Distance", 0);

        isClosed = getIntent().getBooleanExtra("IsClosed", false);

        titleTextView.setText(name);
        ratingTextView.setText(rating + " Stars");
        if (isClosed) {
            isClosedTextView.setText("Closed");
        } else {
            isClosedTextView.setText("Open");
        }
        distanceTextView.setText(distance + "m");

        new DownloadImageTask(image)
                .execute(imageURL);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(SelectedActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 100);
//        }
    }

    @Override
    public void onClick(View view) {
        //Search on google maps
        switch (view.getId()) {
            case R.id.googleMapsButton:
                Uri maps = Uri.parse("http://www.google.com/maps/search/" + titleTextView.getText() + "/@" +
                        latitude + "," + longitude);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, maps);
                Log.d("web", maps.toString());
                Log.d("web", longitude + ", " + latitude);
                getResult.launch(webIntent);
                break;
            case R.id.saveListing:
                //Save listing by inserting into saved database
                db.insertSavedData(name, "saved", imageURL, isClosed, rating, price, location, latitude, longitude, distance);
                Toast.makeText(this, "Listing Saved", Toast.LENGTH_SHORT).show();
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

    /*** Implemented from
     * https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
     * to load image urls into image view
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}