package com.example.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Deliver extends AppCompatActivity {

    private TextView order;

    private FusedLocationProviderClient fusedLocationClient;

    private DeliverCoordinates deliver;
    private AppDatabase database;

    private LatLng myLocation;
    private Order currentOrder;

    private void InsertDatabase(Order order){
        order.uid = getUnUsedId();
        database.getOrderDao().insertAll(order);
    }

    private void UpdatePickUp() {
        if(currentOrder != null)
            database.getOrderDao().updatePickUp(true,currentOrder.uid);
    }

    private void UpdateDelivered() {
        if(currentOrder != null)
            database.getOrderDao().updateDelivered(true,currentOrder.uid);
    }
    private Order getLast(){
        List<Order> orders = database.getOrderDao().getAll();
        return orders.get(orders.size() - 1 );
    }

    private int getUnUsedId() {
        return database.getOrderDao().getAll().size() + 10000;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        order = findViewById(R.id.Order);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        database = Room.databaseBuilder(this, AppDatabase.class, "db-orders")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
        order.setText("Apasa pe Cauta Comanda pentru a livra o comanda!");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("STATE", "" +location.getLatitude());

                            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
        Button searchButton = findViewById(R.id.cautaButton);
        searchButton.setOnClickListener(view -> {
            order.setText("Asteapta cateva secunde pana gasim o comanda pentru tine!");
            AndroidNetworking.initialize(getApplicationContext());
            if(currentOrder == null && myLocation != null){
                getOrder(myLocation.latitude, myLocation.longitude);
            }
            Log.e("STATE", "heloooooo");

            Snackbar.make(view, "Preia pachetul de la adresa celui care o trimite!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        Button pickUpButton = findViewById(R.id.preluataButton);
        pickUpButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Deliver.this);
            builder.setMessage("Sigur ai preluat comanda?").setTitle("Confirmare actiune");
            builder.setPositiveButton("DA", (dialog, id) -> {
                Snackbar.make(view, "Livreaza pachetul de la adresa celui care o primeste!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                deliver.pickUpAlready = true;
                UpdatePickUp();
            });
            builder.setNegativeButton("Renunta", (dialog, id) -> { });
            AlertDialog dialog = builder.create();
            builder.show();
        });

        Button deliveredButton = findViewById(R.id.livrataButton);
        deliveredButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Deliver.this);
            builder.setMessage("Sigur ai livrat comanda?").setTitle("Confirmare actiune");
            builder.setPositiveButton("DA", (dialog, id) ->
            {
                Snackbar.make(view, "Comanda livrata cu succes! Apasa pe Cauta Comanda pentru a face alta livrare!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                deliver.deliveredAlready = true;
                UpdateDelivered();
            });
            builder.setNegativeButton("Renunta", (dialog, id) -> { });
            AlertDialog dialog = builder.create();
            builder.show();
        });
    }


    private void getOrder(double lat, double lng) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("https://delivery-app-deploy2.azurewebsites.net/best-delivery")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("STATE", "hei");

                        try {
                            if(response.getBoolean("success")){
                                JSONObject result = response.getJSONObject("result");
                                JSONObject sender = result.getJSONObject("sender");
                                JSONObject receiver = result.getJSONObject("sender");

                                deliver = new DeliverCoordinates();

                                order.setText("Numarul de telefon al celui care trimite comanda: " + sender.getString("phone") + "\n");
                                order.append("Numele lui este: " + sender.getString("firstName") + " "+ sender.getString("lastName") + "\n");
                                order.append("Adresa acestuia este: " + sender.getString("adress"));
                                deliver.latitudePickUp = sender.getDouble("lat");
                                deliver.longitudePickUp = sender.getDouble("lng");
                                order.append("\n");
                                order.append("\n");

                                order.append("Numarul de telefon al celui care primeste comanda: " + receiver.getString("phone") + "\n");
                                order.append("Numele lui este: " + receiver.getString("firstName") + " "+ receiver.getString("lastName") + "\n");
                                order.append("Adresa acestuia este: " + receiver.getString("adress"));
                                deliver.latitudeDropOff = receiver.getDouble("lat");
                                deliver.longitudeDropOff = receiver.getDouble("lng");
                                order.append("\n");
                                order.append("\n");

                                order.append("Detaiile de livrare sunt: \n");
                                order.append(result.getString("details")+ "\n");
                                order.append("Coletul are greutate de: "+ result.getInt("weight") + " kg \n");

                                Order order = new Order( 231,result.getString("details"), result.getInt("weight"),
                                        sender.getString("phone"),
                                        sender.getDouble("lat"), sender.getDouble("lng")
                                        , sender.getString("firstName"), sender.getString("lastName"), sender.getString("adress")
                                        ,receiver.getString("phone"),
                                        receiver.getDouble("lat"), receiver.getDouble("lng")
                                        , receiver.getString("firstName"), receiver.getString("lastName"),
                                        receiver.getString("adress"), false, false);
                                currentOrder = order;
                                InsertDatabase(order);
                            }
                            else{
                                order.setText("Se pare ca a intervenit o eroare, incearca mai tarziu!\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            order.setText("Se pare ca a intervenit o eroare, incearca mai tarziu!\n");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e("STATE", "iar eroare");
                        Log.e("STATE", error.getErrorBody());
                        order.setText("Se pare ca a intervenit o eroare, incearca mai tarziu!\n");
                    }
                });
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
