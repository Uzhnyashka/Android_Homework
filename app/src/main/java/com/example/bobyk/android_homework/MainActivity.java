package com.example.bobyk.android_homework;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by bobyk on 22/07/15.
 */
public class MainActivity extends ActionBarActivity
        implements OnMapReadyCallback,
        View.OnClickListener,
        GoogleMap.OnMapLongClickListener,
        LocationListener
        {

    private GoogleMap mGoogleMap;
    private Button btnZoomIn, btnZoomOut, btnLocation;
    SharedPreferences sp;
    private LocationManager mLocationManager;
    private LatLng mLocationPosition = null;
    public List<Address> ad;

    public ArrayList<LatLng> markers, mark;
    boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews(){
        btnZoomIn   = (Button) findViewById(R.id.btn_ZoomIn);
        btnZoomOut  = (Button) findViewById(R.id.btn_ZoomOut);
        btnLocation = (Button) findViewById(R.id.btn_Location);

        btnZoomIn   .setOnClickListener(this);
        btnZoomOut  .setOnClickListener(this);
        btnLocation .setOnClickListener(this);

        markers = new ArrayList<>();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        start = true;
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(this);
        setMarkers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.markers_del:
                deleteMarkers();
                break;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapLongClickListener(this);
        if (start){
            findViews();
            getMarkers();
            start = false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ZoomIn:
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;

            case R.id.btn_ZoomOut:
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;

            case R.id.btn_Location:
                if (mLocationPosition != null) getInfoCurrentLocation();
                break;

            }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        putMarker(latLng);
    }

    private void setMarkers(){
        String json = new Gson().toJson(markers);
        sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("MarkerList", json);
        editor.apply();
    }

    private void getMarkers() {
        mark = new ArrayList<>();
        Type type = new TypeToken<ArrayList<LatLng>>() {}.getType();
        sp = getPreferences(MODE_PRIVATE);
        String json = sp.getString("MarkerList", "");
        if (json != null) {
            mark = new Gson().fromJson(json, type);
            for (int i = 0; i < mark.size(); i++) {
                LatLng latLng = mark.get(i);
                putMarker(latLng);
            }
        }
    }

    public void deleteMarkers(){
        markers.clear();
        mark.clear();
        sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        mGoogleMap.clear();
    }



    public void getInfoCurrentLocation(){
        final Dialog dialog = new Dialog(this, R.style.cust_dialog);
        dialog.setContentView(R.layout.info_location_dialog);
        dialog.setTitle("Location info");

        dialog.setCanceledOnTouchOutside(true);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            ad = geocoder.getFromLocation(mLocationPosition.latitude, mLocationPosition.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String country = ad.get(0).getCountryName();
        TextView txtCountry = (TextView) dialog.findViewById(R.id.txtCountry);
        txtCountry.setText("Country: " + country);
        String state = ad.get(0).getAdminArea();
        TextView txtState = (TextView) dialog.findViewById(R.id.txtState);
        txtState.setText("State: " + state);
        String address = ad.get(0).getAddressLine(0);
        TextView txtAddress = (TextView) dialog.findViewById(R.id.txtAddress);
        txtAddress.setText("Address: " + address);

        dialog.show();
    }




    private void putMarker(LatLng latLng){
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker()));
        markers.add(latLng);
    }

            @Override
            public void onLocationChanged(Location location) {
                mLocationPosition = new LatLng(location.getLatitude(),location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }
