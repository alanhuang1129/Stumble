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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.location.LocationListener;
import android.widget.TextView;

import org.w3c.dom.Text;

//Activity responsible for acting as the page with the listing description and details
public class SelectedActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private Button googleMapsButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude = 49.1828152;
    private double longitude = -122.9739637;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        googleMapsButton = (Button) findViewById(R.id.googleMapsButton);
        googleMapsButton.setOnClickListener(this);

        titleTextView = (TextView) findViewById(R.id.titleTextView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SelectedActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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