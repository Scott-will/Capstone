package com.example.cyclesafejava.Services;

import android.content.Context;

import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;

public class SettingsService {

    public void Save(Context context, Settings settings){
        JsonFileHandler.writeSettings(settings, context.getFilesDir());
    }

    public Settings LoadData(String path){
        return JsonFileHandler.readSettings(path);
    }

}
