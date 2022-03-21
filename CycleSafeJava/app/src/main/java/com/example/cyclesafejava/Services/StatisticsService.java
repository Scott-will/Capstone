package com.example.cyclesafejava.Services;


import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.data.Statistics;

public class StatisticsService {

    public Statistics statistics;

    public Statistics LoadData(String path){
        return JsonFileHandler.readStatistics(path);
    }
}
