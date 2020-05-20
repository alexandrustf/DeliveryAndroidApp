package com.example.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Deliver extends AppCompatActivity {

    private TextView order;
    private JSONObject bestDeliveryResponse;

    private double deliverNowLat;
    private double deliverNowLng;
    private DeliverCoordinates deliver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        order = findViewById(R.id.Order);

        order.setText("Apasa pe Cauta Comanda pentru a livra o comanda!");

        Button searchButton = findViewById(R.id.cautaButton);
        searchButton.setOnClickListener(view -> {
            order.setText("Asteapta cateva secunde pana gasim o comanda pentru tine!");
            AndroidNetworking.initialize(getApplicationContext());
            getOrder(47.151726, 27.587914);
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
