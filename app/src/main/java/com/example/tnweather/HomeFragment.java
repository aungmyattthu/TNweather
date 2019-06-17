package com.example.tnweather;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
        weatherRespones = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getContext(),weatherRespones);
        weatherAdapter.setRecyclerItemClickListener(new WeatherAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(ListItem weatherResponse) {
                Intent intent = new Intent(getActivity(), Detail.class);
                intent.putExtra("weatherData",weatherResponse);
                intent.putExtra("weatherData",weatherResponse);
                intent.putExtra("temperature",String.valueOf(Math.round(weatherResponse.getMain().getTemp())));
                intent.putExtra("status",weatherResponse.getWeather().get(0).getMain());
                intent.putExtra("todaydate",weatherResponse.date());
                intent.putExtra("humidity",String.valueOf(weatherResponse.getMain().getHumidity()));
                intent.putExtra("pressure",String.valueOf(weatherResponse.getMain().getPressure()));
                intent.putExtra("windspeed", String.valueOf(Math.round(weatherResponse.getWind().getKilometer()*100)/100.0));
                intent.putExtra("img",weatherResponse.getWeather().get(0).getIcon());
                startActivity(intent);
            }
        });
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
       weatherRespones.clear();
       weatherRespones.addAll(weatherArrayList);
       weatherAdapter.notifyDataSetChanged();
    }


    @Override
    public void errorView(Throwable throwable) {
   //     Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
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
}
