package com.example.tnweather.restClient;

import com.example.tnweather.model.WeatherRespone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api")
    Call<WeatherRespone> getAllItems();
}

