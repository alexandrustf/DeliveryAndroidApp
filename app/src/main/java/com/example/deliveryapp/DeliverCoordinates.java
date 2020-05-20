package com.example.deliveryapp;

public class DeliverCoordinates {

    public double latitudePickUp;
    public double longitudePickUp;

    public double latitudeDropOff;
    public double longitudeDropOff;

    public boolean pickUpAlready ;
    public boolean deliveredAlready;

    public DeliverCoordinates() {
        pickUpAlready = false;
        deliveredAlready = false;
    }

    public DeliverCoordinates(double latitudePickUp, double longitudePickUp, double latitudeDropOff, double longitudeDropOff) {
        this.latitudePickUp = latitudePickUp;
        this.longitudePickUp = longitudePickUp;
        this.latitudeDropOff = latitudeDropOff;
        this.longitudeDropOff = longitudeDropOff;
        pickUpAlready = false;
        deliveredAlready = false;
    }

    public double getLatitudePickUp() {
        return latitudePickUp;
    }

    public void setLatitudePickUp(double latitudePickUp) {
        this.latitudePickUp = latitudePickUp;
    }

    public double getLongitudePickUp() {
        return longitudePickUp;
    }

    public void setLongitudePickUp(double longitudePickUp) {
        this.longitudePickUp = longitudePickUp;
    }

    public double getLatitudeDropOff() {
        return latitudeDropOff;
    }

    public void setLatitudeDropOff(double latitudeDropOff) {
        this.latitudeDropOff = latitudeDropOff;
    }

    public double getLongitudeDropOff() {
        return longitudeDropOff;
    }

    public void setLongitudeDropOff(double longitudeDropOff) {
        this.longitudeDropOff = longitudeDropOff;
    }
}
