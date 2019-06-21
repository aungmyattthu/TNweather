package com.example.tnweather.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tnweather.R;
import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerAdapter.MyViewHolder> {
   private List<ListItem> weatherResponses;
   private Context context;

    public DetailRecyclerAdapter(List<ListItem> weatherResponses, Context context) {
        this.weatherResponses = weatherResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.temperature.setText(String.valueOf(Math.round(weatherResponses.get(position).getMain().getTemp())));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(weatherResponses.get(position).getDt() * 1000L);
        holder.status.setText(weatherResponses.get(position).getWeather().get(0).getMain());

        holder.date.setText(weatherResponses.get(position).getDtTxt());
        Glide.with(context).load("http://openweathermap.org/img/w/"+weatherResponses.get(position).getWeather().get(0).getIcon()+".png").into(holder.weatherImg);

    }

    @Override
    public int getItemCount() {
        return weatherResponses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public @BindView(R.id.temperature)
        TextView temperature;
        public @BindView(R.id.status)
        TextView status;
        public @BindView(R.id.weather_img)
        ImageView weatherImg;
        public @BindView(R.id.date)
        TextView date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/Futura Heavy Regular.ttf");

            temperature.setTypeface(custom_font);
            status.setTypeface(custom_font);
            date.setTypeface(custom_font);
        }
    }
}
