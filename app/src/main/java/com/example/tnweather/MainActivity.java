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
   // private WeatherListImpl weatherList;
    public TinyDB tinyDB;
    //@BindView(R.id.locationTv)
    public TextView locationTv;
    private LocationManager locationManager;
    private Location lastLocation;
    private boolean mFirstUse = true;
    private static final String FIRST_TIME = "first_time";


    public FusedLocationProviderClient fusedLocationClient;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
                   getSupportFragmentManager().beginTransaction().add(R.id.main_container, HomeFragment.newInstance()).addToBackStack("home").commit();
                    return  true;
                case R.id.navigation_dashboard:
                   getSupportFragmentManager().beginTransaction().add(R.id.main_container, AboutFragment.newInstance()).addToBackStack("about").commit();
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

        //weatherList = new WeatherListImpl(this);

        if(!tinyDB.getBoolean("firstTime"))
        {
            //Toast.makeText(this, "leeepyan", Toast.LENGTH_SHORT).show();
           // Toast.makeText(this, tinyDB.getBoolean("firstTime")+"", Toast.LENGTH_SHORT).show();
            tinyDB.putBoolean("firstTime",true);
            firstUse();
            //Log.d("Latitude",tinyDB.getString("Latitude"));
            //Log.d("Latitude",tinyDB.getString("Longitude"));


        }
        else
        {
            //Toast.makeText(this, "sapapyan", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, tinyDB.getString("Latitude")+"", Toast.LENGTH_SHORT).show();
            //Log.d("Latitude",tinyDB.getString("Latitude"));
            //Log.d("Latitude",tinyDB.getString("Longitude"));

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
                /*Toast.makeText(this, "Location Test:"+lastLocation.toString(), Toast.LENGTH_LONG).show();*/
                //locationTv.setText(lastLocation.getLatitude()+""+"   "+lastLocation.getLongitude());
                tinyDB.putString("Latitude",String.valueOf(lastLocation.getLatitude()));
                tinyDB.putString("Longitude",String.valueOf(lastLocation.getLongitude()));
                Toast.makeText(this, "latitute:" + tinyDB.getString("Latitude"), Toast.LENGTH_SHORT).show();
                //test();
            }
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            tinyDB.putString("Latitude",String.valueOf(lastLocation.getLatitude()));
            tinyDB.putString("Longitude",String.valueOf(lastLocation.getLongitude()));
        }

        //  tinyDB.putBoolean("firstTime",false);

    }


    private void test(){
        Toast.makeText(this, "latitute:" + tinyDB.getString("Latitude"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

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

   /* private void markAppUsed() {
        tinyDB.putBoolean("firstTime",true);

    }*/

}

