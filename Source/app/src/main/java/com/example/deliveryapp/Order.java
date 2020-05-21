package com.example.deliveryapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "order")
public class Order {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "details")
    public String details;

    @ColumnInfo(name = "weight")
    public double weight;

    @ColumnInfo(name = "phoneSender")
    public String phoneSender;

    @ColumnInfo(name = "latitudeSender")
    public double latitudeSender;

    @ColumnInfo(name = "longitudeSender")
    public double longitudeSender;

    @ColumnInfo(name = "firstNameSender")
    public String firstNameSender;

    @ColumnInfo(name = "lastNameSender")
    public String lastNameSender;

    @ColumnInfo(name = "adressSender")
    public String adressSender;

    @ColumnInfo(name = "phoneReceiver")
    public String phoneReceiver;

    @ColumnInfo(name = "latitudeReceiver")
    public double latitudeReceiver;

    @ColumnInfo(name = "longitudeReceiver")
    public double longitudeReceiver;

    @ColumnInfo(name = "firstNameReceiver")
    public String firstNameReceiver;

    @ColumnInfo(name = "lastNameReceiver")
    public String lastNameReceiver;

    @ColumnInfo(name = "adressReceiver")
    public String adressReceiver;

    @ColumnInfo(name = "pickUpAlready")
    public boolean pickUpAlready ;

    @ColumnInfo(name = "deliveredAlready")
    public boolean deliveredAlready;

    public Order() {
    }

    @Ignore
    public Order(int uid, String details, double weight, String phoneSender, double latitudeSender, double longitudeSender, String firstNameSender, String lastNameSender, String adressSender, String phoneReceiver, double latitudeReceiver, double longitudeReceiver, String firstNameReceiver, String lastNameReceiver, String adressReceiver, boolean pickUpAlready, boolean deliveredAlready) {
        this.uid = uid;
        this.details = details;
        this.weight = weight;
        this.phoneSender = phoneSender;
        this.latitudeSender = latitudeSender;
        this.longitudeSender = longitudeSender;
        this.firstNameSender = firstNameSender;
        this.lastNameSender = lastNameSender;
        this.adressSender = adressSender;
        this.phoneReceiver = phoneReceiver;
        this.latitudeReceiver = latitudeReceiver;
        this.longitudeReceiver = longitudeReceiver;
        this.firstNameReceiver = firstNameReceiver;
        this.lastNameReceiver = lastNameReceiver;
        this.adressReceiver = adressReceiver;
        this.pickUpAlready = pickUpAlready;
        this.deliveredAlready = deliveredAlready;
    }
}

