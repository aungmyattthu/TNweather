package com.example.tnweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.tnweather.model.TinyDB;
import com.example.tnweather.view.WeatherView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements WeatherView {

    private TextView mTextMessage;

    private TinyDB tinyDB;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                 getSupportFragmentManager().beginTransaction().replace(R.id.main_container,HomeFragment.newInstance()).addToBackStack("home").commit();
                    return true;
                case R.id.navigation_dashboard:
                  getSupportFragmentManager().beginTransaction().replace(R.id.main_container,AboutFragment.newInstance()).addToBackStack("about").commit();
                    return true;

                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,HomeFragment.newInstance()).addToBackStack("home").commit();
                    return true;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tinyDB = new TinyDB(this);

    }

    @Override
    public void loadingView() {

    }

    @Override
    public void hideloading() {

    }

    @Override
    public void errorView() {

    }
}
