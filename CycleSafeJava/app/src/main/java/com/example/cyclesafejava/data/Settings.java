package com.example.cyclesafejava.data;

public class Settings {
    //bluetooth
    public String DeviceID;

    //components
    public boolean Brake;
    public boolean Turn;
    public boolean BatteryNotif;

    public Settings(String name){
        this.DeviceID = name;
        this.Brake = true;
        this.Turn = true;
        this.BatteryNotif = true;
    }

    public Settings(){
        DeviceID = "";
        this.Brake = true;
        this.Turn = true;
        this.BatteryNotif = true;
    }

}
