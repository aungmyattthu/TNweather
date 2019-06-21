package com.example.tnweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tnweather.adapter.DetailRecyclerAdapter;
import com.example.tnweather.adapter.WeatherAdapter;
import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.presenter.WeatherDetailPresenter;
import com.example.tnweather.presenter.WeatherResponePresenter;
import com.example.tnweather.view.MainContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Detail extends AppCompatActivity implements MainContract.View{
    public @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    public @BindView(R.id.temperature)
    TextView temperature;
    public @BindView(R.id.status)
    TextView status;
    public @BindView(R.id.txt_humidity)
    TextView humidity;
    public @BindView(R.id.txt_pressure)
    TextView pressure;
    public @BindView(R.id.txt_windspeed)
    TextView windspeed;

    public @BindView(R.id.weather_img)
    ImageView weatherImg;
    public @BindView(R.id.today_date)
    TextView todayDate;

    private Intent intent;

    private WeatherResponePresenter presenter;
    private List<ListItem> weatherRespones;
    private DetailRecyclerAdapter detailRecyclerAdapter;
    private WeatherDetailPresenter weatherDetailPresenter;
    private  DetailRecyclerAdapter.DetailedRecyclerItemClickListener onCickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Weather Forecast");
        initUI();
        presenter = new WeatherResponePresenter(this,new WeatherListImpl(this));
        presenter.requestDataFromServer();

    }

    public void initUI(){
       onCickListener = new DetailRecyclerAdapter.DetailedRecyclerItemClickListener() {
           @Override
           public void onItemClickz(ListItem weatherResponse) {
               Toast.makeText(Detail.this, "lee pl", Toast.LENGTH_SHORT).show();
           }
       };
            weatherRespones = new ArrayList<>();
            detailRecyclerAdapter = new DetailRecyclerAdapter(weatherRespones,this);
            LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
            intent= getIntent();
            temperature.setText(intent.getStringExtra("temperature"));
            status.setText(intent.getStringExtra("status"));
            todayDate.setText(intent.getStringExtra("todaydate"));
            humidity.setText("Humidity "+ intent.getStringExtra("humidity")+" %");
            pressure.setText("Pressure " + intent.getStringExtra("pressure")+" hPa");
            windspeed.setText("Wind Speed " + intent.getStringExtra("windspeed")+" km/hour");
        Glide.with(this).load("http://openweathermap.org/img/w/"+intent.getStringExtra("img")+".png").into(weatherImg);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(detailRecyclerAdapter);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadingView() {
    }

    @Override
    public void hideloading() {

    }

    @Override
    public void setDataToRecyclerView(List<ListItem> weatherArrayList, WeatherResponse weatherResponse) {
        weatherRespones.clear();
        List<ListItem> result = new ArrayList<>();
        for(ListItem weather:weatherArrayList)


        {
            if(weather.date().equals(intent.getStringExtra("todaydate"))){
                result.add(weather);
                Log.i("data",weather.date());
            }
        }

        Log.i("data",intent.getStringExtra("todaydate"));
        Log.i("data",result.toString());
        weatherRespones.addAll(result);
        detailRecyclerAdapter.notifyDataSetChanged();
    }



    /*@Override
    public void setDataToRecyclerViewInDetail(List<ListItem> weatherArrayList) {
        weatherRespones.clear();
        List<ListItem> result = new ArrayList<>();
        for(ListItem weather:weatherArrayList)
        {
            if(weather.date().equals(intent.getStringExtra("todaydate"))){
                result.add(weather);
                Log.i("data",weather.date());
            }
        }

        Log.i("data",intent.getStringExtra("todaydate"));
        Log.i("data",result.toString());
        weatherRespones.addAll(result);
        detailRecyclerAdapter.notifyDataSetChanged();
    }*/



    @Override
    public void errorView(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
