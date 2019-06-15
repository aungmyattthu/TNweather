package com.example.tnweather.adapter;

import android.content.Context;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tnweather.R;
import com.example.tnweather.model.ListItem;

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
    public WeatherAdapter(Context context, List<ListItem> weatherRespones, RecyclerItemClickListener recyclerItemClickListener) {
        this.weatherResponses = weatherRespones;
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


            holder.temperature.setText(String.valueOf(Math.round(weatherResponses.get(position*8).getMain().getTemp())));

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
