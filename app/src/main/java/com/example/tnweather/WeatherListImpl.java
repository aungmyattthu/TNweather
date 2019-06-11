package com.example.tnweather;

import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherRespone;
import com.example.tnweather.restClient.ApiService;
import com.example.tnweather.restClient.RestClient;
import com.example.tnweather.view.MainContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherListImpl implements MainContract.Model {
    @Override
    public void getWeatherList(final OnFinishListener onFinishListener) {
        ApiService apiService = RestClient.getRetrofit().create(ApiService.class);

        Call<WeatherRespone> call = apiService.getAllItems("latitude","longitude");
        call.enqueue(new Callback<WeatherRespone>() {
            @Override
            public void onResponse(Call<WeatherRespone> call, Response<WeatherRespone> response) {
                List<WeatherRespone> weatherRespones = (List<WeatherRespone>) response.body();
                onFinishListener.onFinished(weatherRespones);
            }

            @Override
            public void onFailure(Call<WeatherRespone> call, Throwable t) {
                onFinishListener.onFailure(t);
            }
        });
    }
}
