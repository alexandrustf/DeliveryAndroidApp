package com.example.deliveryapp;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NavRoute extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private AppDatabase database;
    private LatLng toGo;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        database = Room.databaseBuilder(this, AppDatabase.class, "db-orders")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
        // Add a marker in Sydney and move the camera
        LatLng iasi = new LatLng(	47.151726, 27.587914);

        Order order = getLast();
        if(order.pickUpAlready == false){
            LatLng sydney = new LatLng(order.latitudeSender, order.longitudeSender);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Go to Sender"));
            toGo = sydney;
        }
        if(order.pickUpAlready == true ){
            LatLng sydney = new LatLng(order.latitudeReceiver, order.longitudeReceiver);
            toGo = sydney;
            mMap.addMarker(new MarkerOptions().position(sydney).title("Go to Receiver"));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(iasi));
        mMap.moveCamera(CameraUpdateFactory.zoomBy(10));

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("STATE", "" +location.getLatitude());
                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(sydney).title("You are here!")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    }
                });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(toGo !=null){
                    String urlToGo = ""  + toGo.latitude+ "," + toGo.longitude;
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + urlToGo);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

    }

    private Order getLast(){
        List<Order> orders = database.getOrderDao().getAll();
        return orders.get(orders.size() - 1 );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deliver) {
            goToOrder();
            return true;
        }

        if (id == R.id.navigation) {
            goToNavigation();
            return true;
        }

        if (id == R.id.orders_history) {
            goToOrdersHistory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToOrdersHistory(){
        Intent intent = new Intent(this, OrdersHistory.class);
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
