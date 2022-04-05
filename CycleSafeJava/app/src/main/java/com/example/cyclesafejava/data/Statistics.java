package com.example.cyclesafejava.data;

public class Statistics {
    public Double LongestRide;
    public Double FastestSpeed;
    public Double TotalDistance;
    public Double CurrentSpeed;
    public Double Distance;

    public Statistics(){

    }

    public Statistics(Double longestRide, Double fastestSpeed, Double totalDistance){
        this.TotalDistance = totalDistance;
        this.FastestSpeed = fastestSpeed;
        this.LongestRide = longestRide;
        this.CurrentSpeed = 0.0;
        this.Distance = 0.0;
    }
}
