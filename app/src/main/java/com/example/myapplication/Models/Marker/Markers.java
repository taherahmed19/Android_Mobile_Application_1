package com.example.myapplication.Models.Marker;

import android.location.Location;

import java.util.ArrayList;

public class Markers {

    public ArrayList<Location> markers;
    int index;

    public Markers(){
        this.markers = new ArrayList<Location>();
        this.index = 0;
    }

    public void addMarker(Location newLocation){
        this.markers.add(newLocation);
    }

    public Location getMarker(int index){
        return this.markers.get(index);
    }

    public void incrementIndex(){
        this.index++;
    }

    public int getIndex(){
        return this.index;
    }

    public void resetIndex(){
        this.index = -1;
    }

    public void setupTestData(){
        Location losAngeles = new Location("");
        Location newYork = new Location("");
        Location london = new Location("");

        losAngeles.setLatitude(34.0201613);
        losAngeles.setLongitude(-118.6919193);
        newYork.setLatitude(40.6971494);
        newYork.setLongitude(-74.2598644);
        london.setLatitude(51.5285582);
        london.setLongitude(-0.2416809);

        this.markers.add(losAngeles);
        this.markers.add(newYork);
        this.markers.add(london);
    }
}
