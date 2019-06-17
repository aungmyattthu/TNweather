package com.example.tnweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements MainContract.View, LocationListener {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }

            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, v);

        initUI();
        presenter = new WeatherResponePresenter(this, new WeatherListImpl(getContext()));
        presenter.requestDataFromServer();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initUI();
                presenter = new WeatherResponePresenter(HomeFragment.this, new WeatherListImpl(getContext()));
                presenter.requestDataFromServer();

                swipeRefreshLayout.setRefreshing(false);


            }
        });


        return v;
    }

    private void initUI() {

        clickListener = new WeatherAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(ListItem weatherRespone) {
                Toast.makeText(getContext(), weatherRespone.getMain().getTemp()+"", Toast.LENGTH_SHORT).show();
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

    public void setDataToRecyclerView(List<ListItem> weatherArrayList, WeatherResponse weatherResponse, Calendar today) {
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
        if (Integer.parseInt(hour) >12){
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
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUI();
                presenter = new WeatherResponePresenter(HomeFragment.this, new WeatherListImpl(getContext()));
                presenter.requestDataFromServer();

            }
        });
    }

    /*private void firstUse() {
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }

        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                tinyDB.putString("Latitude", String.valueOf(lastLocation.getLatitude()));
                tinyDB.putString("Longitude", String.valueOf(lastLocation.getLongitude()));

            }


        }
    }*/

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
}
