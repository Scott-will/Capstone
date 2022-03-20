package com.example.cyclesafejava.ViewModels;

import android.content.Context;

import com.example.cyclesafejava.Services.StatisticsService;
import com.example.cyclesafejava.data.Statistics;

public class StatisticsViewModel {
    private StatisticsService statisticsService = new StatisticsService();

    public StatisticsViewModel(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    public Statistics LoadStatistics(Context context){
        return this.statisticsService.LoadData(context.getPackageResourcePath());
    }
}
