package com.example.tnweather.restClient;



import com.example.tnweather.model.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("forecast?")
    Call<WeatherResponse> getAllItems(@Query("appid") String appid, @Query("lat") String lat, @Query("lon") String lon);

}

