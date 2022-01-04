package com.example.cyclesafejava.Json;

import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;

import org.json.JSONObject;

public class JsonParser {
    public static Settings ParseSettings(String jsonString){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            Settings settings = new Settings();
            settings.DeviceID = jsonObject.getString("DeviceID");
            return settings;
        }
        catch(Exception e){
            return new Settings();
        }
    }

    public  static Statistics ParseStatistics(String jsonString){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            Statistics statistics = new Statistics();
            statistics.FastestSpeed = jsonObject.getDouble("FastestSpeed");
            statistics.LongestRide = jsonObject.getDouble("LongestRide");
            statistics.TotalDistance = jsonObject.getDouble("TotalDistance");
            return statistics;
        }
        catch(Exception e){
            return new Statistics();
        }
    }
}
