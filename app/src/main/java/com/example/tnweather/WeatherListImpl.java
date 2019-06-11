package com.example.tnweather;

import com.example.tnweather.model.TinyDB;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.restClient.ApiService;
import com.example.tnweather.restClient.RestClient;
import com.example.tnweather.view.MainContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherListImpl implements MainContract.Model {
    private TinyDB tinyDB;
    private String appId = "41843abc3a0a307b7d4fc345c7d05270"; // api key
    @Override
    public void getWeatherList(final OnFinishListener onFinishListener) {
        ApiService apiService = RestClient.getRetrofit().create(ApiService.class);
        Call<WeatherResponse> call = apiService.getAllItems(appId, tinyDB.getString("Latitute"), tinyDB.getString("Longitute"));
        call.enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                List<WeatherResponse> weatherRespones = (List<WeatherResponse>) response.body();
                onFinishListener.onFinished(weatherRespones);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                onFinishListener.onFailure(t);
            }
        });
    }
}
