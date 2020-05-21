package com.example.deliveryapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Order.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OrderDao getOrderDao();
}