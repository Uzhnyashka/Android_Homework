package com.example.bobyk.android_homework;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by bobyk on 22/07/15.
 */
public class MainActivity extends ActionBarActivity
        implements OnMapReadyCallback,
        View.OnClickListener,
        GoogleMap.OnMapLongClickListener{

    private GoogleMap mGoogleMap;
    private Button btnZoomIn, btnZoomOut;
    SharedPreferences sp;

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

        btnZoomIn   .setOnClickListener(this);
        btnZoomOut  .setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        start = true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setMarkers();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapLongClickListener(this);
        if (start){
            Log.d("kek","getSavedMarkers");
            findViews();
            markers = new ArrayList<>();
            getMarkers();
            start = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        putMarker(latLng);
    }

    private void putMarker(LatLng latLng){
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker()));
        markers.add(latLng);
    }
}
