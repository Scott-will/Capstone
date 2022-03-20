package com.example.cyclesafejava.data.Events;

import com.example.cyclesafejava.data.Statistics;

public class StatisticsEvent {
    private double speed;
    private double fastestSpeed;
    private float distance;

    public StatisticsEvent(double speed, float distance, double fastestSpeed){
        this.speed = speed;
        this.fastestSpeed = fastestSpeed;
        this.distance = distance;
    }

    public double getSpeed(){
        return this.speed;
    }

    public double getFastestSpeed() {
        return this.fastestSpeed;
    }

    public float getDistance() {
        return this.distance;
    }
}
