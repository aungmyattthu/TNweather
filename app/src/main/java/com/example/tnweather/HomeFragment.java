package com.example.tnweather;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tnweather.adapter.WeatherAdapter;
import com.example.tnweather.model.TinyDB;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.presenter.WeatherResponePresenter;
import com.example.tnweather.view.MainContract;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class HomeFragment extends Fragment implements MainContract.View {

    private WeatherResponePresenter presenter;
    private List<WeatherResponse> weatherRespones;
    private WeatherAdapter weatherAdapter;
    private WeatherAdapter.RecyclerItemClickListener clickListener;
    private TinyDB tinyDB;
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    private FusedLocationProviderClient fusedLocationClient;

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


        tinyDB = new TinyDB(container.getContext());
        initUI();
        /*locationTv = findViewById(R.id.location);*/


        if (!tinyDB.getBoolean("firstTime")) {
            firstUse();
            //Toast.makeText(container.getContext(), "sapa pyan", Toast.LENGTH_LONG).show();

        } else {
            //Toast.makeText(container.getContext(), "lee pyan", Toast.LENGTH_LONG).show();
            Toast.makeText(container.getContext(), tinyDB.getString("Latitute"), Toast.LENGTH_LONG).show();
        }


        presenter = new WeatherResponePresenter(this);
        presenter.requestDataFromServer();
        return v;
    }

    private void initUI() {
        clickListener = new WeatherAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(WeatherResponse weatherRespone) {
                Toast.makeText(getContext(), weatherRespone.getCity() + "", Toast.LENGTH_SHORT).show();
            }
        };
        weatherRespones = new ArrayList<>();
        weatherAdapter = new WeatherAdapter((ArrayList<WeatherResponse>) weatherRespones, clickListener);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

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
    public void setDataToRecyclerView(List<WeatherResponse> weatherArrayList) {
        weatherRespones.addAll(weatherArrayList);
        weatherAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorView(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void firstUse() {


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


        if (checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            /*locationTv.setText(location.toString());*/
                            tinyDB.putString("Latitute", String.valueOf(location.getLatitude()));
                            tinyDB.putString("Longitute", String.valueOf(location.getLongitude()));
                            Toast.makeText(getContext(), "leee pl:" + location.toString(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext(), "fuck you!!", Toast.LENGTH_SHORT).show();
                    }
                });
        markAppUsed();


    }

    private void markAppUsed() {
        tinyDB.putBoolean("firstTime",true);

    }
}
