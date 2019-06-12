package com.example.tnweather;

import android.Manifest;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.tnweather.model.TinyDB;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TinyDB tinyDB;
    private TextView locationTv;

    private boolean mFirstUse = false;
    private static final String FIRST_TIME = "first_time";


    private FusedLocationProviderClient fusedLocationClient;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(MainActivity.this, "I am homefragment", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, HomeFragment.newInstance()).addToBackStack("home").commit();
                    break;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, AboutFragment.newInstance()).addToBackStack("about").commit();
                    Toast.makeText(MainActivity.this, "I am about fragment", Toast.LENGTH_SHORT).show();
                   break;
                default:

                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,HomeFragment.newInstance()).addToBackStack("home").commit();

                    break;
            }
            return true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        /*tinyDB = new TinyDB(this);
        *//*locationTv = findViewById(R.id.location);*//*


        if(!tinyDB.getBoolean("firstTime")){
            Toast.makeText(this, "sapa pyan", Toast.LENGTH_LONG).show();
            firstUse();
        }
        else
            Toast.makeText(this, "lee pyan", Toast.LENGTH_SHORT).show();*/







    }

    private void firstUse() {


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            locationTv.setText(location.toString());
                            tinyDB.putString("Latitute", String.valueOf(location.getLatitude()));
                            tinyDB.putString("Longitute", String.valueOf(location.getLongitude()));
                        }
                    }
                });

        markAppUsed();

    }

    private void markAppUsed() {
        tinyDB.putBoolean("firstTime",true);

    }

}

