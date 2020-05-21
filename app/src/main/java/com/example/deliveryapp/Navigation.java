package com.example.deliveryapp;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

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

import java.util.List;

public class Navigation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        database = Room.databaseBuilder(this, AppDatabase.class, "db-orders")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
        // Add a marker in Sydney and move the camera
        Log.e("STATE", "ello");
        Order order = getLast();
        if(order.pickUpAlready == false){

            LatLng sydney = new LatLng(order.latitudeSender, order.longitudeSender);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sender"));

        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("STATE", "" +location.getLatitude());

                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in my loc"));
                            float zoomLevel = 20.0f; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                        }
                    }
                });
    }

    private Order getLast(){
        List<Order> orders = database.getOrderDao().getAll();
        return orders.get(orders.size() - 1 );
    }
}
