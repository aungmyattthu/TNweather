package com.example.tnweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tnweather.model.ListItem;
import com.example.tnweather.model.Main;
import com.example.tnweather.model.WeatherResponse;
import com.example.tnweather.view.MainContract;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionErrorFragment extends Fragment implements MainContract.View {
    @BindView(R.id.permissionBtn)
    public Button permissionBtn;


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
        ButterKnife.bind(this,v);
        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package",getActivity().getPackageName(), null);
                intent.setData(uri);
                getContext().startActivity(intent);


            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // No explanation needed; request the permission
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

                //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            }}
        else {
            Toast.makeText(getContext(), "hey I got a permission", Toast.LENGTH_LONG).show();
            ((MainActivity)getActivity()).replaceFragment();
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

    }

    @Override
    public void errorView(Throwable t) {

    }
}
