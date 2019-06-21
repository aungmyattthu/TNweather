package com.example.tnweather;

import android.content.Intent;
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

public class HomeFragment extends Fragment implements MainContract.View{

    private WeatherResponePresenter presenter;
    private List<ListItem> weatherRespones;
    private WeatherAdapter weatherAdapter;
    private TinyDB tinyDB;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

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
        tinyDB = new TinyDB(getActivity());
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
       locationText.setText(weatherResponse.getCity().getName());

        Long calendar = Calendar.getInstance().getTimeInMillis();
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
    //    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
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



}
