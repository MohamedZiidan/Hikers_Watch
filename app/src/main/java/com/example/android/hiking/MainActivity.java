package com.example.android.hiking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitudeText;
    TextView longitudeText;
    TextView accuracyText;
    TextView altitudeText;
    TextView addressText;
    double latitude;
    double longitude;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationUpdate(lastKnownLocation);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeText = findViewById(R.id.LatitudeTextView);
        longitudeText = findViewById(R.id.LongtudeTextView);
        accuracyText = findViewById(R.id.AccuracyTextView);
        altitudeText = findViewById(R.id.AltitudeTextView);
        addressText = findViewById(R.id.AddressTextView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                locationUpdate(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationUpdate(lastKnownLocation);
        }


    }


    public void locationUpdate(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        latitudeText.setText("Latitude : " + Double.toString(latitude));
        longitudeText.setText("Longitude : " + Double.toString(longitude));
        accuracyText.setText("Accuracy : " + Double.toString(location.getAccuracy()));
        altitudeText.setText("Altitude : " + Double.toString(location.getAltitude()));


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String message = "We can't find an address :(";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {

                message = "Address :\n";

                if (addresses.get(0).getThoroughfare() != null) {
                    message += addresses.get(0).getThoroughfare() + "\n";
                }

                if (addresses.get(0).getLocality() != null) {
                    message += addresses.get(0).getLocality() + " ";
                }

                if (addresses.get(0).getAdminArea() != null) {
                    message += addresses.get(0).getAdminArea();
                }

                addressText.setText(message);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}