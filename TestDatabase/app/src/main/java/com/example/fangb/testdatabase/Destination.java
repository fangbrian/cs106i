package com.example.fangb.testdatabase;

import android.location.Location;

/**
 * Created by fangb on 8/23/2014.
 */
public class Destination {
    private String currName;
    private Location currLocation = new Location("");
    private boolean isValid;

    public Destination(String name, double longitude, double latitude, boolean valid){
        currName = name;
        currLocation.setLongitude(longitude);
        currLocation.setLatitude(latitude);
        isValid = valid;
    }

    public void setName(String name){
        currName = name;
    }

    public void setLong(double longitude){
        currLocation.setLongitude(longitude);
    }

    public void setLat(double latitude){
        currLocation.setLatitude(latitude);
    }

    public void setValid(boolean b){
        isValid = b;
    }

    public void clearAll(){
        currName = "";
        currLocation = new Location("");
        isValid = false;
    }

    public boolean valid(){
        return isValid;
    }

    public void setInvalid(){
        isValid = false;
    }

    public double getLong(){
        return currLocation.getLongitude();
    }

    public double getLat(){
        return currLocation.getLatitude();
    }

    public String getName(){
        return currName;
    }

    public Location getLocation(){
        return currLocation;
    }
}
