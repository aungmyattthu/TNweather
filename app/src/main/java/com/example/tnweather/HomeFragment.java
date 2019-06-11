package com.example.tnweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tnweather.adapter.WeatherAdapter;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.presenter.WeatherResponePresenter;
import com.example.tnweather.view.MainContract;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements MainContract.View {

    private WeatherResponePresenter presenter;
    private List<WeatherResponse> weatherRespones;
    private WeatherAdapter weatherAdapter;
    private WeatherAdapter.RecyclerItemClickListener clickListener;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    public ProgressBar progressBar;

    @BindView(R.id.main_container)
    public FrameLayout frameLayout;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment,container,false);
        ButterKnife.bind(this,v);
        initUI();
        presenter = new WeatherResponePresenter(this);
        presenter.requestDataFromServer();

        return v;
    }

    private void initUI(){
        clickListener = new WeatherAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(WeatherResponse weatherRespone) {
                Toast.makeText(getContext(), weatherRespone.getCity()+"", Toast.LENGTH_SHORT).show();
            }
        };
        weatherRespones = new ArrayList<>();
        weatherAdapter = new WeatherAdapter((ArrayList<WeatherResponse>) weatherRespones,clickListener);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(weatherAdapter);

    }

    @Override
    public void loadingView() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideloading() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(List<WeatherResponse> weatherArrayList) {
       weatherRespones.addAll(weatherArrayList);
       weatherAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorView(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
