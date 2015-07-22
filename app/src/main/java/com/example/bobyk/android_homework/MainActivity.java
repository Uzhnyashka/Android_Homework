package com.example.bobyk.android_homework;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by bobyk on 22/07/15.
 */
public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, View.OnClickListener{

    private GoogleMap mGoogleMap;
    private Button btnZoomIn, btnZoomOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMap();
    }

    private void findViews(){
        btnZoomIn   = (Button) findViewById(R.id.btn_ZoomIn);
        btnZoomOut  = (Button) findViewById(R.id.btn_ZoomOut);

        btnZoomIn   .setOnClickListener(this);
        btnZoomOut  .setOnClickListener(this);
    }

    private void initMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        findViews();
        mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
