package com.example.cyclesafejava.Services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.cyclesafejava.data.Events.Event;
import com.example.cyclesafejava.data.Events.LocationEvent;
import com.example.cyclesafejava.data.Events.StatisticsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MapsService extends Service {
    private float distance;
    private Location currentLocation;
    private Location lastKnownLocation;
    private double speed;
    private double fastestSpeed;
    private long start;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        while(1 != 2){
            this.CalculateDistance();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(LocationEvent e){
        this.currentLocation = e.getCurrentLocation();
        this.lastKnownLocation = e.getlastKnownLocation();
        float distanceTravelled = lastKnownLocation.distanceTo(currentLocation);
        this.distance += distanceTravelled;
        long finish = System.nanoTime();
        CalculateSpeed(start, finish, distanceTravelled);
        this.sendRiderStatistics();
    }

    private void CalculateDistance(){
        while(true){
            this.start = System.nanoTime();
            this.getDeviceLocation();


        }
    }

    private void CalculateSpeed(long start, long finish, float distanceTravelled){
        this.speed = (distanceTravelled/(finish-start))/Math.pow(10, 9);
        if(this.speed > fastestSpeed){
            this.fastestSpeed = speed;
        }
    }

    private void getDeviceLocation(){
        //build event to activity that returns location
        EventBus.getDefault().post(new Event(0, Event.LOCATION));
    }


    private void sendRiderStatistics(){
        EventBus.getDefault().post(new StatisticsEvent(this.speed, this.distance, this.fastestSpeed));
    }


}
