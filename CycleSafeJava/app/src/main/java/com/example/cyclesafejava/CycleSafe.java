package com.example.cyclesafejava;

import android.app.Application;

import com.google.android.gms.maps.MapsInitializer;

public class CycleSafe extends Application {
    public AppContainer appContainer = new AppContainer();

    @Override
    public void onCreate(){
        super.onCreate();
        MapsInitializer.initialize(this);
    }
}
