package com.example.tnweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tnweather.adapter.WeatherAdapter;
import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.TinyDB;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.presenter.WeatherResponePresenter;
import com.example.tnweather.view.MainContract;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements MainContract.View,LocationListener{

    private boolean locationOff = true;

    private WeatherResponePresenter presenter;
    private List<ListItem> weatherRespones;
    private WeatherAdapter weatherAdapter;
    private WeatherAdapter.RecyclerItemClickListener clickListener;

    private TinyDB tinyDB;
    private LocationManager locationManager;
    private Location lastLocation;
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    private FusedLocationProviderClient fusedLocationClient;

    @BindView(R.id.progress_bar)
    public ProgressBar progressBar;
    @BindView(R.id.location)
    public TextView locationText;

    @BindView(R.id.updateTime)
    public TextView updateTime;

    @BindView(R.id.retryBtn)
    public Button retryBtn;

    @BindView(R.id.ErrorText)
    public TextView errorText;

    @BindView(R.id.linearLayout)
    public ConstraintLayout linearLayout;

    @BindView(R.id.swipeView)
    public SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, v);
        tinyDB = new TinyDB(getContext());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);


        refreshLocation();
        //setData();

       /* if(tinyDB.getString("Latitude")!= "" && tinyDB.getString("Longitude")!= null){*/
            /*initUI();
            presenter = new WeatherResponePresenter(this, new WeatherListImpl(getContext()));
            presenter.requestDataFromServer();*/

       /* }
        else{
            customErrorView();
        }*/
       retryBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setData();
           }
       });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(tinyDB.getString("Latitude")!= "" && tinyDB.getString("Longitude")!= null){
                    //refreshLocation();
                    //initUI();
                    setData();
                    /*presenter = new WeatherResponePresenter(HomeFragment.this, new WeatherListImpl(getContext()));
                    presenter.requestDataFromServer();*/

                    swipeRefreshLayout.setRefreshing(false);
                }
                else{
                    customErrorView();
                    //errorText.setText("something might wrong!");
                }



            }
        });


        return v;
    }

    private void initUI() {


        clickListener = new WeatherAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(ListItem weatherResponse) {
                //Toast.makeText(getContext(), weatherResponse.getMain().getTemp()+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Detail.class);
                intent.putExtra("dt",weatherResponse.getDt());
                intent.putExtra("weatherData",weatherResponse);
                intent.putExtra("weatherData",weatherResponse);
                intent.putExtra("temperature",String.valueOf(Math.round(weatherResponse.getMain().getTemp())));
                intent.putExtra("status",weatherResponse.getWeather().get(0).getMain());
                intent.putExtra("time",weatherResponse.getDtTxt());
                intent.putExtra("todaydate",weatherResponse.date());
                intent.putExtra("humidity",String.valueOf(weatherResponse.getMain().getHumidity()));
                intent.putExtra("pressure",String.valueOf(weatherResponse.getMain().getPressure()));
                intent.putExtra("windspeed", String.valueOf(Math.round(weatherResponse.getWind().getKilometer()*100)/100.0));
                intent.putExtra("img",weatherResponse.getWeather().get(0).getIcon());
                startActivity(intent);
            }
        };


        weatherRespones = new ArrayList<>();
        weatherAdapter = new WeatherAdapter((ArrayList<ListItem>) weatherRespones,clickListener,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(weatherAdapter);




        }

    @Override
    public void loadingView() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        retryBtn.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

    }

    @Override
    public void hideloading() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        retryBtn.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override

    public void setDataToRecyclerView(List<ListItem> weatherArrayList, WeatherResponse weatherResponse) {
       weatherRespones.addAll(weatherArrayList);
        weatherAdapter.notifyDataSetChanged();
        //weatherArrayList.get(0).
        locationText.setText(weatherResponse.getCity().getName());


        Long calendar = Calendar.getInstance().getTimeInMillis();
        //Long calendar = today.getTimeInMillis();
        //Long difference = calendarNow - calendar;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String s = simpleDateFormat.format(calendar);
        SimpleDateFormat simple = new SimpleDateFormat("HH");
        String hour = simple.format(calendar);
        if (Integer.parseInt(hour) >=12){
            updateTime.setText("Updated at "+s +" PM");
        }
        else
        {
            updateTime.setText("Updated at "+s +" AM");
        }

    }

    @Override
    public void errorView(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
       // recyclerView.setVisibility(View.VISIBLE);
        //progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorText.setText("No Connection!");
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

    }
    public void customErrorView(){
        errorText.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();


            }
        });
    }

    public void setData(){

        if(locationOff == true){
            if(tinyDB.getString("Latitude")!= ""){
                initUI();
                presenter = new WeatherResponePresenter(this, new WeatherListImpl(getContext()));
                presenter.requestDataFromServer();
            }
            else{
                customErrorView();
                errorText.setText("No Location Data!!");
            }
        }
        else {

            initUI();
            presenter = new WeatherResponePresenter(this, new WeatherListImpl(getContext()));
            presenter.requestDataFromServer();
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("sapa pl", "onLocationChanged: "+location.getLongitude());
        tinyDB.putString("Latitude",String.valueOf(location.getLatitude()));
        tinyDB.putString("Longitude",String.valueOf(location.getLongitude()));
        setData();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        locationOff = false;
//        setData();
    }

    @Override
    public void onProviderDisabled(String provider) {
        if(tinyDB.getString("Latitude") == null){
            customErrorView();
            errorText.setText("Location disabled!!");
        }


    }

    public void refreshLocation(){
        if(Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    customErrorView();
                    errorText.setText("wahahaha");
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    // No explanation needed; request the permission
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                    customErrorView();
                    errorText.setText("lol");
                    //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


                }

            } else
            {
               // Toast.makeText(getContext(), "Location granted", Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,900000,2000,this);
                lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(locationOff == false){
                    tinyDB.putString("Latitude",String.valueOf(lastLocation.getLatitude()));
                    tinyDB.putString("Longitude",String.valueOf(lastLocation.getLongitude()));
                   // Toast.makeText(getContext(), "fuck you", Toast.LENGTH_SHORT).show();

                }

            }

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       /* if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    Toast.makeText(this, "Location granted", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                *//*ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);*//*
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,PermissionErrorFragment.newInstance()).commit();
            }
        }*/

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            initUI();
            presenter = new WeatherResponePresenter(this, new WeatherListImpl(getContext()));
            presenter.requestDataFromServer();
            // This is Case 2 (Permission is now granted)
        } else {
            /*((MainActivity)getActivity()).replaceFragment();*/
            //getFragmentManager().beginTransaction().replace(R.id.main_container,PermissionErrorFragment.newInstance()).commit();
            // This is Case 1 again as Permission is not granted by user
            customErrorView();
            errorText.setText("You need to allow Location Permission!");
            //Now further we check if used denied permanently or not
            if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity)getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // case 4 User has denied permission but not permanently
                //recyclerView.
                customErrorView();
                errorText.setText("You need to allow Location Permission!");

            } else {
                // case 5. Permission denied permanently.
                // You can open Permission setting's page from here now.
                //((MainActivity)getActivity()).replaceFragment();
                //getFragmentManager().beginTransaction().replace(R.id.main_container,PermissionErrorFragment.newInstance()).commit();
                customErrorView();
                errorText.setText("You need to allow permission!");
            }

        }

    }
    }

