package com.example.tnweather.restClient;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api")
    Call<Object> getAllItems();
}

