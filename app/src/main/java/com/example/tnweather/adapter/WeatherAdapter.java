package com.example.tnweather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tnweather.R;
import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<ListItem> weatherRespones;
    private RecyclerItemClickListener recyclerItemClickListener;

    public WeatherAdapter(ArrayList<ListItem> weatherRespones, RecyclerItemClickListener recyclerItemClickListener) {
        this.weatherRespones = weatherRespones;
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
        holder.temperature.setText(weatherRespones.get(position).getMain().getTempKf());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemClick(weatherRespones.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
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
        void onItemClick(ListItem weatherRespone);
    }
}
