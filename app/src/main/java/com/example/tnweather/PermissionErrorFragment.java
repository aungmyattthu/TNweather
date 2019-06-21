package com.example.tnweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.view.MainContract;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PermissionErrorFragment extends Fragment implements MainContract.View {

    public static PermissionErrorFragment newInstance(){
        Bundle args = new Bundle();
        PermissionErrorFragment fragment = new PermissionErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.permission_error_fragment, container , false);
       return v;
    }

    @Override
    public void loadingView() {

    }

    @Override
    public void hideloading() {

    }

    @Override
    public void setDataToRecyclerView(List<ListItem> weatherArrayList, WeatherResponse weatherResponse) {

    }

    @Override
    public void errorView(Throwable t) {

    }
}
