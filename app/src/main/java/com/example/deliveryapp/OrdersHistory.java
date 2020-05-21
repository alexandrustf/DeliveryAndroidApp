package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OrdersHistory extends AppCompatActivity {
    private AppDatabase database ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = Room.databaseBuilder(this, AppDatabase.class, "db-orders")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Order> list = CreateDatabase();
                String some = "da aici: ";
                for (Order product : list)
                {

                    some += product.details;

                }
                Snackbar.make(view, some, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ListView listView = (ListView)findViewById(R.id.history_orders);

        ArrayList<Order> list = CreateDatabase();
        TextView textView = findViewById(R.id.textView);

        final ArrayList<String> arrayList = new ArrayList<>();
        for (Order product : list)
        {
            arrayList.add("Comanda trimisa de: " + product.firstNameSender + product.lastNameSender + "\n"+
//                    "from adress: " + product.adressSender + " to adress: " + product.adressReceiver + "\n"+
                     " cu urmatoarele detalii: " + product.details +
                    " status: preluat-> " + product.pickUpAlready + " livrat-> " + product.deliveredAlready);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(OrdersHistory.this, "clicked item" + position
                                +" " + arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
    private int  globalId = 0;
    private ArrayList<Order> CreateDatabase(){

        Order order = new Order( 23,"2 misto super smcher 2", 10.3,"3213123",
                47.151726, 27.587914
                , "gigi", "popescu", "pe sararie","3213123",
                47.151726, 27.587914
                , "gigi", "popescu", "pe sararie", false, false);

//        database.getOrderDao().insertAll(order);

        return (ArrayList<Order>) database.getOrderDao().getAll();
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
        return super.onOptionsItemSelected(item);
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
