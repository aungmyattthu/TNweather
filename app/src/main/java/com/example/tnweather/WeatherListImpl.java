package com.example.tnweather;


import com.example.tnweather.model.ListItem;
import android.content.Context;
import com.example.tnweather.model.TinyDB;
import com.example.tnweather.model.WeatherItem;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.restClient.ApiService;
import com.example.tnweather.restClient.RestClient;
import com.example.tnweather.view.MainContract;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherListImpl implements MainContract.Model {
    private TinyDB tinyDB;
    private String appId = "41843abc3a0a307b7d4fc345c7d05270"; // api key
    private Context context;

    public WeatherListImpl(Context context) {
        this.context = context;
    }

    /* public WeatherListImpl(Context context) {
        this.context = context;
    }*/

    @Override
    public void getWeatherList(final OnFinishListener onFinishListener) {
        tinyDB = new TinyDB(context);
        ApiService apiService = RestClient.getRetrofit().create(ApiService.class);
        Call<WeatherResponse> call = apiService.getAllItems(appId, tinyDB.getString("Latitude"), tinyDB.getString("Longitude"),"metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherRespones = response.body();
                List<ListItem> weatherData = weatherRespones.getList();
                onFinishListener.onFinished(weatherData, weatherRespones );

            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                onFinishListener.onFailure(t);
            }
        });
    }
}
