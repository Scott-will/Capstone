package com.example.cyclesafejava.Services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.example.cyclesafejava.CycleSafe;
import com.example.cyclesafejava.Logger;
import com.example.cyclesafejava.data.Events.Event;
import com.example.cyclesafejava.data.Events.LocationEvent;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

public class MapsService extends Service {
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            EventBus.getDefault().register(this);
        }
        catch(Exception e){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        try{
            EventBus.getDefault().register(this);
        }
        catch(Exception e){

        }
        Calculator calculator = new Calculator();
        calculator.start();
        return START_NOT_STICKY;
    }


    public class Calculator extends Thread{

        private double distance;
        private Location currentLocation;
        private Location lastKnownLocation;
        private double speed;
        private double fastestSpeed;
        private long start;

        private void getDeviceLocation() {
            EventBus.getDefault().post(new Event(0, Event.LOCATION));
        }

        @Override
        public void run(){
            try{
                EventBus.getDefault().register(this);
            }
            catch(Exception e){

            }
            this.CalculateDistance();
        }

        private void CalculateDistance(){
            while(true){
                if(Looper.getMainLooper().getThread() == Thread.currentThread()){
                    Logger.debug("Im in main thread");
                }
                try{
                    Thread.sleep(3000);
                    this.start = System.nanoTime();
                    this.getDeviceLocation();
                }
                catch (Exception e){
                    Logger.debug(e.getMessage());
                }
            }
        }

        @Subscribe(threadMode = ThreadMode.BACKGROUND)
        public void onEvent(LocationEvent e) {
            if(Looper.getMainLooper().getThread() == Thread.currentThread()){
                Logger.debug("Im in main thread");
            }
            //if last known null else use previous stored last known
            if(lastKnownLocation == null){
                lastKnownLocation = e.getlastKnownLocation();
            }
            else{
                start -= 3*Math.pow(10, 9);
            }
            currentLocation = e.getCurrentLocation();
            double distanceTravelled = distance(
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), 'K');
            if(distanceTravelled == Double.NaN){
                return;
            }
            distance += distanceTravelled;
            long finish = System.nanoTime();
            this.CalculateSpeed(start, finish, distanceTravelled);
        }

        private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == 'K') {
                dist = dist * 1.609344;
            } else if (unit == 'N') {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        /*::  This function converts radians to decimal degrees             :*/
        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }

        private void CalculateSpeed(long start, long finish, double distanceTravelled){
            if(Looper.getMainLooper().getThread() == Thread.currentThread()){
                Logger.debug("Im in main thread");
            }
            this.speed = Math.abs(distanceTravelled/(finish-start))*3.6*Math.pow(10, 11);
            if(this.speed > fastestSpeed){
                this.fastestSpeed = speed;
            }
            try{
                sendRiderStatistics();
            }
            catch (Exception e){
                Logger.debug(e.getMessage());
            }
        }

        private void sendRiderStatistics(){
            if(Looper.getMainLooper().getThread() == Thread.currentThread()){
                Logger.debug("Im in main thread");
            }
            EventBus.getDefault().post(new Event(0, Event.STATSTICS, this.speed, this.distance, this.fastestSpeed));
        }
    }



}
