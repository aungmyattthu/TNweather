package com.example.tnweather.adapter;

import android.content.Context;
import android.graphics.Typeface;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tnweather.R;
import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherResponse;


import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<ListItem> weatherResponses;
    private RecyclerItemClickListener recyclerItemClickListener;
    private Context context;
    public WeatherAdapter(Context context, List<ListItem> weatherRespones) {
        this.weatherResponses = weatherRespones;
        this.context = context;
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


            holder.temperature.setText(String.valueOf(Math.round(weatherResponses.get(position*8).getMain().getTemp())));
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(weatherResponses.get(position*8).getDt() * 1000L);
            String date = DateFormat.format("dd", cal).toString();
            holder.status.setText(weatherResponses.get(position*8).getWeather().get(0).getMain());
            holder.date.setText(String.valueOf(date));
            holder.day.setText(DateFormat.format("EEE",cal));
            Glide.with(context).load("http://openweathermap.org/img/w/"+weatherResponses.get(position*8).getWeather().get(0).getIcon()+".png").into(holder.weathericon);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i= holder.getAdapterPosition();
                recyclerItemClickListener.onItemClick(weatherResponses.get(i*8));
            }
        });

    }

    @Override
    public int getItemCount() {
        return weatherResponses.size()/8;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public @BindView(R.id.temperature)
        TextView temperature;
        public @BindView(R.id.status)
        TextView status;
        public @BindView(R.id.day)
        TextView day;
        public @BindView(R.id.date)
        TextView date;
        public @BindView(R.id.weather_img)
        ImageView weathericon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/Futura Heavy Regular.ttf");

            temperature.setTypeface(custom_font);
            status.setTypeface(custom_font);
            day.setTypeface(custom_font);
            date.setTypeface(custom_font);
        }
    }


   public interface RecyclerItemClickListener{
        void onItemClick(ListItem weatherResponse);
    }

   }

