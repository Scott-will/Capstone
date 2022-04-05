package com.example.cyclesafejava.Services;


import android.content.Context;

import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.data.Statistics;

public class StatisticsService {

    public Statistics LoadData(String path){
        return JsonFileHandler.readStatistics(path);
    }

    public void SaveStatistics(Context context, Statistics statistics){
        JsonFileHandler.writeStatistics(statistics, context.getFilesDir());
    }
}
