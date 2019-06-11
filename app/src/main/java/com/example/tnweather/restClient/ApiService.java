package com.example.tnweather.restClient;

import com.example.tnweather.model.WeatherRespone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api.openweathermap.org/data/2.5/forecast")
    Call<WeatherRespone> getAllItems(@Query("lat")String latitude,@Query("lon")String longitude);
}

