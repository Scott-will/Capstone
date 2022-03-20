package com.example.cyclesafejava.data.Events;

import android.location.Location;

public class LocationEvent {
    private Location currentLocation;
    private Location lastKnownLocation;

    public LocationEvent(Location currentLocation, Location lastKnownLocation){
        this.currentLocation = currentLocation;
        this.lastKnownLocation = lastKnownLocation;
    }

    public Location getCurrentLocation(){
        return this.currentLocation;
    }

    public Location getlastKnownLocation(){
        return this.lastKnownLocation;
    }
}
