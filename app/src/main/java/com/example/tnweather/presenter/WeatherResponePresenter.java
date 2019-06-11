package com.example.tnweather.presenter;

import com.example.tnweather.WeatherListImpl;

import com.example.tnweather.model.WeatherResponse;

import com.example.tnweather.view.MainContract;

import java.util.List;

public class WeatherResponePresenter implements MainContract.Presenter, MainContract.Model.OnFinishListener {

    private MainContract.View  weatherView;
    private MainContract.Model weatherListModel;

    public WeatherResponePresenter(MainContract.View weatherView) {
        this.weatherView = weatherView;
        weatherListModel = new WeatherListImpl();
    }


    @Override
    public void onFinished(List<WeatherResponse> weatherArrayList) {
        weatherView.setDataToRecyclerView(weatherArrayList);
        if (weatherView != null)
        {
            weatherView.hideloading();
        }

    }

    @Override
    public void onFailure(Throwable t) {
        weatherView.errorView(t);
        if (weatherView != null)
        {
            weatherView.hideloading();
        }

    }

    @Override
    public void onDestroy() {
        this.weatherView = null;
    }

    @Override
    public void getMoreData() {
        if (weatherView != null)
        {
            weatherView.loadingView();
        }
        weatherListModel.getWeatherList(this);
    }

    @Override
    public void requestDataFromServer() {
        if (weatherView != null)
        {
            weatherView.loadingView();
        }
        weatherListModel.getWeatherList(this);

    }
}
