package com.example.cyclesafejava.data.Events;

public class Event {
    //need message for: connect, start ride, distance/speed update, position update
    //place marker
    public static final String CONNECT = "CONNECT";

    public static final String START_RIDE="START_RIDE";

    public static final String DISTANCE_SPEED = "DISTANCE_SPEED";

    public static final String LOCATION = "LOCATION";

    public static final String BATTERY = "BATTERY";

    public static final String STATSTICS = "STATISTICS";

    public double speed;
    public double fastestSpeed;
    public double distance;

    int mResult;
    String mResultValue;

    public Event(int resultCode, String resultValue){
        mResult = resultCode;
        mResultValue = resultValue;
    }

    public Event(int resultCode, String resultValue, double speed, double distance, double fastestSpeed){
        mResult = resultCode;
        mResultValue = resultValue;
        this.speed = speed;
        this.fastestSpeed = fastestSpeed;
        this.distance = distance;
    }

    public int getResult(){
        return mResult;
    }

    public String getmResultValue(){
        return mResultValue;
    }


}
