package user.com.cus.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import user.com.cus.Adapter.SearchLocationAdapter;
import user.com.cus.DataModel.Place.PlaceData;
import user.com.cus.DataModel.Place.PlaceModel;
import user.com.cus.DataModel.Place.PlaceResult;
import user.com.cus.DataModel.Place.SearchLocation;
import user.com.cus.Listener.SearchLocationListener;
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

public class PointMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "cekPointMapsActivity";
    private GoogleMap mMap;

    private int[] icon = new int[]{
            R.drawable.fnb_cir,
            R.drawable.stationery_cir,
            R.drawable.mart_cir
    };

    LocationListener locationListener;
    LocationManager locationManager;

    List<PlaceResult> listPlace;
    List<Marker> listMarker;

    Call<PlaceModel> callPlace;
    SearchLocationAdapter adapter;

    boolean myButtonIsClick = false;
    @BindView(R.id.edt_search) EditText edtSearch;

    @BindView(R.id.root_search) RelativeLayout rootSearchLocation;
    @BindView(R.id.progress_search) ProgressBar progressSearch;
    @BindView(R.id.rv_search) RecyclerView rvSearch;
    @BindView(R.id.txt_no_result) TextView txtNoResult;

    private boolean isTyping;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isTyping = false;
            searchLocation();
        }
    };

    private void searchLocation() {
        rootSearchLocation.setVisibility(View.VISIBLE);
        progressSearch.setVisibility(View.VISIBLE);
        rvSearch.setVisibility(View.GONE);
        txtNoResult.setVisibility(View.GONE);
        List<SearchLocation> listSearch = new ArrayList<>();

        String keywords = edtSearch.getText().toString().toLowerCase();
        for (PlaceResult place : listPlace){
            String placeName = place.getName().toLowerCase();
            if (placeName.contains(keywords)){
                listSearch.add(new SearchLocation(place.getName(), place.getAddress(), place.getLatitude(), place.getLongitude()));
            }
        }

        if (listSearch.size() == 0){
            txtNoResult.setVisibility(View.VISIBLE);
            rvSearch.setVisibility(View.GONE);
        }else{
            txtNoResult.setVisibility(View.GONE);
            rvSearch.setVisibility(View.VISIBLE);
            adapter.refreshData(listSearch);
        }

        progressSearch.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ButterKnife.bind(this);
        settingEditTextSearchLocation();

        Utils.closeSoftKeyboard(getApplicationContext(), getCurrentFocus());

        rvSearch.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvSearch.setLayoutManager(llm);

        adapter = new SearchLocationAdapter(new ArrayList<SearchLocation>(), new SearchLocationListener() {
            @Override
            public void onItemSearchClicked(SearchLocation searchLocation) {
                closeSearchResult();
                Utils.closeSoftKeyboard(getApplicationContext(), getCurrentFocus());
                edtSearch.setText(searchLocation.getName());
                edtSearch.setCursorVisible(false);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(searchLocation.getLat(), searchLocation.getLng())));
                handler.removeCallbacks(runnable);

                for (Marker marker : listMarker){
                    if (marker.getPosition().latitude == searchLocation.getLat() && marker.getPosition().longitude == searchLocation.getLng()){
                        marker.showInfoWindow();
                        break;
                    }
                }
            }
        });
        rvSearch.setAdapter(adapter);
        closeSearchResult();

    }

    private void closeSearchResult(){
        rootSearchLocation.setVisibility(View.GONE);
        progressSearch.setVisibility(View.GONE);
        rvSearch.setVisibility(View.GONE);
        txtNoResult.setVisibility(View.GONE);
    }

    private void settingEditTextSearchLocation() {
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSearch.setCursorVisible(true);
            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                edtSearch.setCursorVisible(true);
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Toast.makeText(PointMapsActivity.this, edtSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(runnable);
                if (edtSearch.getText().length() > 3){
                    handler.postDelayed(runnable, 1000);
                    if (!isTyping){
                        isTyping = true;
                    }
                }else{
                    isTyping = false;
                    //dibawah 3 karakter
                    closeSearchResult();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            checkLocationPermission();
        }
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

    private void checkLocationPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("cekLocation", "location permission granted");
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                getLocationData();
            } else {
                ActivityCompat.requestPermissions(PointMapsActivity.this,
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
        mMap.setMyLocationEnabled(true);
        Location lastKnownLocation;
        lastKnownLocation = mMap.getMyLocation();
        if (lastKnownLocation == null){
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation == null){
                    //locationnnya null
                    Toast.makeText(getApplicationContext(), "System can't detect your location. Please press your location on Maps", Toast.LENGTH_SHORT).show();
                    Log.d("cekListPlace", "getLocationData: null");
                    return;
                }
            }
        }

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Location location =  mMap.getMyLocation();
                if (location == null){
                    Toast.makeText(PointMapsActivity.this, "GPS is not ready yet. Please try again!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (!myButtonIsClick){
                    mMap.clear();
                    callPointPlaceData(location.getLatitude(), location.getLongitude(),0);
                    myButtonIsClick = true;
                }
                return false;
            }
        });

        double lat = lastKnownLocation.getLatitude();
        double lng = lastKnownLocation.getLongitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15));
        listPlace = new ArrayList<>();
        listMarker = new ArrayList<>();
        callPointPlaceData(lat,lng,0);
    }

    private void callPointPlaceData(final double lat, final double lng, final int category) {

        PlaceData placeData = new PlaceData(category, lat, lng, 0, 0.1);
        callPlace = RetrofitServices.sendListPlaceRequest().callListPlace(Utils.sharedJsonHeader, Utils.sharedHeader, placeData);
        callPlace.enqueue(new Callback<PlaceModel>() {
            @Override
            public void onResponse(Call<PlaceModel> call, Response<PlaceModel> response) {
                if (!callPlace.isCanceled()){
                    if (response.body() != null){
                        if (response.body().getError() != null){
                            Toast.makeText(getApplicationContext(), response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: "+response.body().getError().getMsg());
                        }else {
                            setDataAfterDataCalled(response.body().getResult(), lat, lng, category);
                            if (category != 2){
                                callPointPlaceData(lat, lng, category + 1);
                            }else{
                                myButtonIsClick = false;
                            }
                        }
                    }else{
                        Log.d(TAG, "onResponse: response was null");
                    }
                }else{
                    Log.d(TAG, "onResponse: canceled");
                }
            }

            @Override
            public void onFailure(Call<PlaceModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });

    }

    private void setDataAfterDataCalled(List<PlaceResult> result, double lat, double lng, int category) {
        LatLng latLng = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));

        listPlace.addAll(result);
        Log.d(TAG, "setDataAfterDataCalled: "+listPlace.size());

        int ic = icon[category];

        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(ic);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        for (int i = 0; i<result.size(); i++){
            LatLng place = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());

            MarkerOptions markerOptions = new MarkerOptions().position(place).title(result.get(i).getName()).snippet(result.get(i).getName()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            listMarker.add(mMap.addMarker(markerOptions));
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int idx = -1;
                for (int i=0; i<listMarker.size(); i++){
                    if (listMarker.get(i).getId().equals(marker.getId())){
                        idx = i;
                        break;
                    }
                }

                if (idx != -1){
                    PlaceResult placeResult = listPlace.get(idx);
                    Log.d("cekMaps", "onMarkerClick: "+"\n"+
                            placeResult.getName());
                    Intent intent = new Intent(getApplicationContext(), ListItemActivity.class);
                    intent.putExtra("idPlace", placeResult.getId());
                    intent.putExtra("imgUrl",placeResult.getImgUrl());
                    intent.putExtra("placeName",placeResult.getName());
                    intent.putExtra("placeAddress",placeResult.getAddress());
                    intent.putExtra("placeContact",placeResult.getPhone());
                    startActivity(intent);
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                edtSearch.setCursorVisible(false);
                Utils.closeSoftKeyboard(getApplicationContext(), getCurrentFocus());
                closeSearchResult();
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (callPlace != null){
            callPlace.cancel();
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        edtSearch.setCursorVisible(false);
    }
}
