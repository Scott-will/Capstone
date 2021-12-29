package com.example.cyclesafejava.Json;

import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;

public class JsonParser {

    public static Settings ParseSettings(){
        return new Settings();
    }

    public static Statistics ParseStatistics(){
        return new Statistics();
    }

}
