package com.example.hitesh.hikerswatch;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latTextView, lonTextView, altTextView, accTextView, addressView;

    String address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView = (TextView)findViewById(R.id.latTextView);
        lonTextView = (TextView)findViewById(R.id.lonTextView);
        accTextView = (TextView)findViewById(R.id.accTextView);
        altTextView = (TextView)findViewById(R.id.altTextView);
        addressView = (TextView)findViewById(R.id.addressView);

        LocationProvider locationProvider;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

        /* Check if GPS LocationProvider supports altitude */
        Log.i("Supports altitude: ", String.valueOf(locationProvider.supportsAltitude()));

        Location location;

        /* Get current location from GPS */
        location = new Location(LocationManager.GPS_PROVIDER);

        /* Check if the requested Location has its altitude set */
        Log.i("Has altitude: ", String.valueOf(location.hasAltitude()));

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                //location = new Location(LocationManager.GPS_PROVIDER);

                address="Location has no address according to Maps.";
                Log.i("Location: ", location.toString());

                latTextView.setText("Latitude: " + String.valueOf(location.getLatitude()));
                lonTextView.setText("Longitude: " + String.valueOf(location.getLongitude()));
                accTextView.setText("Accuracy: " + String.valueOf(location.getAccuracy()));
                altTextView.setText("Altitude: " + String.valueOf(location.getAltitude()));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try{
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if(listAddresses!=null && listAddresses.size()>0){

                        address ="Address:\n";

                        Log.i("Place Info:", listAddresses.get(0).toString());
                        Log.i("Log Altitude: ", String.valueOf(location.getAltitude()));

                        if(listAddresses.get(0).getSubThoroughfare()!= null)
                        {
                            address += listAddresses.get(0).getSubThoroughfare() + ",\n";
                        }

                        if(listAddresses.get(0).getThoroughfare()!= null)
                        {
                            address += listAddresses.get(0).getThoroughfare() + ",\n";
                        }

                        if(listAddresses.get(0).getLocality()!= null)
                        {
                            address += listAddresses.get(0).getLocality() + ",\n";
                        }

                        if(listAddresses.get(0).getPostalCode()!= null)
                        {
                            address += listAddresses.get(0).getPostalCode() + ",\n";
                        }

                        if(listAddresses.get(0).getCountryName()!= null)
                        {
                            address += listAddresses.get(0).getCountryName();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                addressView.setText(address);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                // we have permission!

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }
    }
}
