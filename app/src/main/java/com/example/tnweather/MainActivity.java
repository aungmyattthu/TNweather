package com.example.tnweather;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.tnweather.model.TinyDB;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements LocationListener {
    public TinyDB tinyDB;
    private LocationManager locationManager;
    private Location lastLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }

            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(MainActivity.this, "I am homefragment", Toast.LENGTH_SHORT).show();
                   getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();
                    return  true;
                case R.id.navigation_dashboard:
                   getSupportFragmentManager().beginTransaction().replace(R.id.main_container, AboutFragment.newInstance()).commit();
                    Toast.makeText(MainActivity.this, "I am about fragment", Toast.LENGTH_SHORT).show();
                   return true;

            }
            return false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tinyDB = new TinyDB(this);
        ButterKnife.bind(this);
        setTitle("5 Days Weather Forecast");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,HomeFragment.newInstance()).commit();

        if(!tinyDB.getBoolean("firstTime"))
        {
            tinyDB.putBoolean("firstTime",true);
            checkLocationSetting();
            firstUse();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,HomeFragment.newInstance()).commit();

        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,HomeFragment.newInstance()).commit();
            Log.i("Latitude",tinyDB.getString("Latitude"));
            Log.i("Longitude",tinyDB.getString("Longitude"));
        }


    }
    private void firstUse() {

        if(Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }

        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
                lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                tinyDB.putString("Latitude",String.valueOf(lastLocation.getLatitude()));
                tinyDB.putString("Longitude",String.valueOf(lastLocation.getLongitude()));
            }
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            tinyDB.putString("Latitude",String.valueOf(lastLocation.getLatitude()));
            tinyDB.putString("Longitude",String.valueOf(lastLocation.getLongitude()));
        }

    }

    public void checkLocationSetting(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationChanged", "onLocationChanged: "+location.getLongitude());
        tinyDB.putString("Latitude",String.valueOf(lastLocation.getLatitude()));
        tinyDB.putString("Longitude",String.valueOf(lastLocation.getLongitude()));
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


}

