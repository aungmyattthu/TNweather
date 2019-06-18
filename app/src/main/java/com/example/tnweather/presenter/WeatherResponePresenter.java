package com.example.tnweather.presenter;

import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherResponse;

import com.example.tnweather.view.MainContract;

import java.util.Calendar;
import java.util.List;

public class WeatherResponePresenter implements MainContract.Presenter, MainContract.Model.OnFinishListener {

    private MainContract.View  weatherView;
    private MainContract.Model weatherListModel;

    public WeatherResponePresenter(MainContract.View weatherView,MainContract.Model weatherListModel) {
        this.weatherView = weatherView;
        this.weatherListModel = weatherListModel;
    }


    @Override
    public void onFinished(List<ListItem> weatherArrayList,WeatherResponse weatherResponse) {
        Calendar calendar = Calendar.getInstance();

        weatherView.setDataToRecyclerView(weatherArrayList,weatherResponse,calendar);
        if (weatherView != null)
        {
            weatherView.hideloading();
        }

    }

    @Override
    public void onFailure(Throwable t) {
        if (weatherView != null)
        {
            weatherView.errorView(t);
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
