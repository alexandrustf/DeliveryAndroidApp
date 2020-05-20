package com.example.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NavRoute extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_route);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng origin = new LatLng(	47.151726, 27.587914);
        LatLng dest = new LatLng(	47.155726, 27.582914);


        mMap.addMarker(new MarkerOptions().position(origin).title("Marker in Iasi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.moveCamera(CameraUpdateFactory.zoomBy(2));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.navigate) {
            goToNavigate();
            return true;
        }

        if (id == R.id.deliver) {
            goToOrder();
            return true;
        }

        if (id == R.id.navigation) {
            goToNavigation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToNavigate(){
        Intent intent = new Intent(this, Navigate.class);
        startActivity(intent);
    }

    private void goToOrder(){
        Intent intent = new Intent(this, Deliver.class);
        startActivity(intent);
    }

    private void goToNavigation(){
        Intent intent = new Intent(this, NavRoute.class);
        startActivity(intent);
    }
}
