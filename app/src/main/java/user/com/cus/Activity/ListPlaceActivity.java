package user.com.cus.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import user.com.cus.Adapter.ListPlaceAdapter;
import user.com.cus.DataModel.Place.PlaceData;
import user.com.cus.DataModel.Place.PlaceModel;
import user.com.cus.DataModel.Place.PlaceResult;
import user.com.cus.Listener.ListPlaceListener;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPlaceActivity extends AppCompatActivity {

    private static final String TAG = "cekListPlaceActivity";
    LocationListener locationListener;
    LocationManager locationManager;

    @BindView(R.id.rv_toko) RecyclerView rvToko;
    @BindView(R.id.progress_toko) ProgressBar progressToko;
    @BindView(R.id.root_data_warning) LinearLayout rootDataWarning;
    @BindView(R.id.txt_warning) TextView txtWarning;
    @BindView(R.id.btn_refresh) Button btnRefresh;

    ListPlaceAdapter listPlaceAdapter;
    List<PlaceResult> listPlace;
    int category;

    Call<PlaceModel> callPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_place);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("categoryName"));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        listPlace = new ArrayList<>();
        rvToko.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvToko.setLayoutManager(llm);

        listPlaceAdapter = new ListPlaceAdapter(listPlace, new ListPlaceListener() {
            @Override
            public void onPlaceClicked(PlaceResult placeResult) {
                Intent intent = new Intent(getApplicationContext(), ListItemActivity.class);
                intent.putExtra("idPlace", placeResult.getId());
                intent.putExtra("imgUrl",placeResult.getImgUrl());
                intent.putExtra("placeName",placeResult.getName());
                intent.putExtra("placeAddress",placeResult.getAddress());
                intent.putExtra("placeContact",placeResult.getPhone());
                startActivity(intent);
            }
        }, ListPlaceActivity.this);
        rvToko.setAdapter(listPlaceAdapter);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            checkLocationPermission();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkLocationPermission() {
        progressToko.setVisibility(View.VISIBLE);
        rootDataWarning.setVisibility(View.GONE);
        rvToko.setVisibility(View.GONE);
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("cekLocation", "location permission granted");
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                getLocationData();
            } else {
                ActivityCompat.requestPermissions(ListPlaceActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Utils.PERMISSION_FOR_LOCATION);
            }
        } else {
            Log.d("cekLocation", "location permission granted");
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            getLocationData();
        }
    }

    private void getLocationData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation == null){
                //locationnnya null
                progressToko.setVisibility(View.GONE);
                rootDataWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Sorry your location isnt ready yet.");
                btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            buildAlertMessageNoGps();
                        } else {
                            checkLocationPermission();
                        }
                    }
                });
                Log.d("cekListPlace", "getLocationData: null");
                return;
            }
        }

        double lat = lastKnownLocation.getLatitude();
        double lng = lastKnownLocation.getLongitude();
        category = getIntent().getIntExtra("category",0);
        callListPlaceData(lat,lng,category);
        Log.d("cekListPlace", "getLocationData: "+lat+", "+lng);

    }

    private void callListPlaceData(double lat, double lng, int category) {
        listPlace = new ArrayList<>();

        PlaceData placeData = new PlaceData(category, lat, lng, 0, 0.1);
        callPlace = RetrofitServices.sendListPlaceRequest().callListPlace(Utils.sharedJsonHeader, Utils.sharedHeader, placeData);
        callPlace.enqueue(new Callback<PlaceModel>() {
            @Override
            public void onResponse(Call<PlaceModel> call, Response<PlaceModel> response) {

                if (!callPlace.isCanceled()){
                    if (response.body() != null){
                        if (response.body().getError() != null){
                            Toast.makeText(ListPlaceActivity.this, response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                        }else{
                            listPlace.addAll(response.body().getResult());
                        }
                    }else{
                        Log.d(TAG, "onResponse: response was null");
                    }
                }else{
                    Log.d(TAG, "onResponse: canceled");
                }

                listPlaceAdapter.refreshData(listPlace);
                progressToko.setVisibility(View.GONE);
                rootDataWarning.setVisibility(View.GONE);
                rvToko.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<PlaceModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.toString());
                progressToko.setVisibility(View.GONE);
                rootDataWarning.setVisibility(View.GONE);
                rvToko.setVisibility(View.VISIBLE);
            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Your GPS seems to be disabled, please enable it first.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        try{
            alert.show();
        }catch (Exception e){
            Toast.makeText(this, "you must turn on your GPS first", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utils.PERMISSION_FOR_LOCATION){
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                getLocationData();
            }else{
                Toast.makeText(this, "Permission Denied to access your Location", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (callPlace != null){
            callPlace.cancel();
        }
        super.onDestroy();
    }
}
