package com.example.deliveryapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM `order`")
    List<Order> getAll();

    @Insert
    void insertAll(Order... orders);

    @Delete
    void delete(Order order);

    @Query("UPDATE `order` SET pickUpAlready=:pickUp WHERE uid = :id")
    void updatePickUp(boolean pickUp, int id);

    @Query("UPDATE `order` SET deliveredAlready=:delivered WHERE uid = :id")
    void updateDelivered(boolean delivered, int id);
}
