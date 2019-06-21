package com.example.tnweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.tnweather.model.TinyDB;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();

            // This is Case 2 (Permission is now granted)
        } else {
            /*((MainActivity)getActivity()).replaceFragment();*/
            //getFragmentManager().beginTransaction().replace(R.id.main_container,PermissionErrorFragment.newInstance()).commit();
            // This is Case 1 again as Permission is not granted by user

            //Now further we check if used denied permanently or not
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // case 4 User has denied permission but not permanently
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, PermissionErrorFragment.newInstance()).commit();

            } else {
                // case 5. Permission denied permanently.
                // You can open Permission setting's page from here now.
                //((MainActivity)getActivity()).replaceFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, PermissionErrorFragment.newInstance()).commit();
            }

        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    firstUse();
                    Toast.makeText(MainActivity.this, "I am homefragment", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();
                    return true;
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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, PermissionErrorFragment.newInstance()).commit();
        firstUse();
        //getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();

        /*if (!tinyDB.getBoolean("firstTime")) {
            tinyDB.putBoolean("firstTime", true);
            firstUse();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();
            Log.i("Latitude", tinyDB.getString("Latitude"));
            Log.i("Longitude", tinyDB.getString("Longitude"));
        }*/


    }

    private void firstUse() {

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();

            }

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, PermissionErrorFragment.newInstance()).commit();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    // No explanation needed; request the permission
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

            } else {
                // Toast.makeText(getContext(), "Location granted", Toast.LENGTH_SHORT).show();

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 900000, 0, this);
                lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();
               /* if (String.valueOf(lastLocation.getLongitude()) != " ") {
                    tinyDB.putString("Latitude", String.valueOf(lastLocation.getLatitude()));
                    tinyDB.putString("Longitude", String.valueOf(lastLocation.getLongitude()));
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();
                    //Toast.makeText(getContext(), lastLocation.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, PermissionErrorFragment.newInstance()).commit();
                }*/


            }
        }}



    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

        @Override
        public void onLocationChanged (Location location){
            Log.d("LocationChanged", "onLocationChanged: " + location.getLongitude());
            tinyDB.putString("Latitude", String.valueOf(lastLocation.getLatitude()));
            tinyDB.putString("Longitude", String.valueOf(lastLocation.getLongitude()));
        }

        @Override
        public void onStatusChanged (String s,int i, Bundle bundle){

        }

        @Override
        public void onProviderEnabled (String s){
            tinyDB.putString("Latitude", String.valueOf(lastLocation.getLatitude()));
            tinyDB.putString("Longitude", String.valueOf(lastLocation.getLongitude()));
            //getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).commit();
        }

        @Override
        public void onProviderDisabled (String s){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, PermissionErrorFragment.newInstance()).commit();
        }

    }


