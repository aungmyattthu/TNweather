package com.example.tnweather.view;

import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherResponse;

import java.util.Calendar;
import java.util.List;

public interface MainContract {

    interface Model{

        interface OnFinishListener {
            void onFinished(List<ListItem> weatherArrayList, WeatherResponse weatherResponse);

            void onFailure(Throwable t);
        }

        void getWeatherList(OnFinishListener onFinishListener);

    }

    interface View{

        void loadingView();
        void hideloading();
        void setDataToRecyclerView(List<ListItem> weatherArrayList, WeatherResponse weatherResponse);
        void errorView(Throwable throwable);



    }

    interface Presenter{
        void onDestroy();
        void getMoreData();
        void requestDataFromServer();
    }


}
