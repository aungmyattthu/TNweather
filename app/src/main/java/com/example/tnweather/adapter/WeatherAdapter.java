package com.example.tnweather.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tnweather.R;
import com.example.tnweather.model.ListItem;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<ListItem> weatherRespones;
    private RecyclerItemClickListener recyclerItemClickListener;
    private Context context;
    private Typeface weatheFont;

    public WeatherAdapter(Context context,List<ListItem> weatherRespones, RecyclerItemClickListener recyclerItemClickListener) {
        this.weatherRespones = weatherRespones;
        this.recyclerItemClickListener = recyclerItemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.temperature.setText(String.valueOf(weatherRespones.get(position).getMain().getTempKf()));
        holder.status.setText(weatherRespones.get(position).getWeather().get(0).getMain());
    //    holder.weatherImg.setText(Html.fromHtml(weatherRespones.get(position).getWeather().get(0).getIcon()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemClick(weatherRespones.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return weatherRespones.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public @BindView(R.id.temperature)
        TextView temperature;
        public @BindView(R.id.status)
        TextView status;
        public @BindView(R.id.day)
        TextView day;
        public @BindView(R.id.weather_img)
        TextView weatherImg;
        public @BindView(R.id.date)
        TextView date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            weatheFont = Typeface.createFromAsset(context.getAssets(),"fonts/weathericon.ttf");
            weatherImg.setTypeface(weatheFont);
        }
    }

   public interface RecyclerItemClickListener{
        void onItemClick(ListItem weatherRespone);
    }

   /* private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = context.getString(R.string.weather_sunny);
            } else {
                icon = context.getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = context.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = context.getString(R.string.weather_drizzle);
                    break;
                case 7 : icon =context.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = context.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = context.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = context.getString(R.string.weather_rainy);
                    break;
            }
        }
     //   weatherIcon.setText(icon);
    }*/
}
