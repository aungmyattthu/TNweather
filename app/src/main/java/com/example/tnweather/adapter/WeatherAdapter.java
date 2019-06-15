package com.example.tnweather.adapter;

import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


    public WeatherAdapter(ArrayList<ListItem> weatherResponses, RecyclerItemClickListener recyclerItemClickListener) {
        this.weatherResponses = weatherResponses;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


            holder.temperature.setText(String.valueOf(Math.round(weatherResponses.get(position*8).getMain().getTemp())));

            /**/
            //Timestamp ts=new Timestamp(weatherResponses.get(position).getDt());


            //Date date=new Date(ts.getTime());

            //String d= new SimpleDateFormat();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(weatherResponses.get(position*8).getDt() * 1000L);
            String date = DateFormat.format("dd", cal).toString();

            holder.date.setText(String.valueOf(date));
            holder.day.setText(DateFormat.format("EEE",cal));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemClick(weatherResponses.get(position));
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


   public interface RecyclerItemClickListener{
        void onItemClick(ListItem weatherResponse);
    }
}
