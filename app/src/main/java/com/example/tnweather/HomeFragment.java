package com.example.tnweather;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tnweather.adapter.WeatherAdapter;
import com.example.tnweather.model.ListItem;
import com.example.tnweather.presenter.WeatherResponePresenter;
import com.example.tnweather.view.MainContract;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements MainContract.View, LocationListener {

    private WeatherResponePresenter presenter;
    private List<ListItem> weatherRespones;
    private WeatherAdapter weatherAdapter;
    private WeatherAdapter.RecyclerItemClickListener clickListener;
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    public ProgressBar progressBar;

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
        initUI();
        presenter = new WeatherResponePresenter(this, new WeatherListImpl(getContext()));
        presenter.requestDataFromServer();
        return v;
    }

    private void initUI() {

        clickListener = new WeatherAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(ListItem weatherRespone) {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }
        };
        weatherRespones = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getContext(),weatherRespones,clickListener);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(weatherAdapter);
        }

    @Override
    public void loadingView() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideloading() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override

    public void setDataToRecyclerView(List<ListItem> weatherArrayList) {
       weatherRespones.addAll(weatherArrayList);
       weatherAdapter.notifyDataSetChanged();



    }

    @Override
    public void errorView(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
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
