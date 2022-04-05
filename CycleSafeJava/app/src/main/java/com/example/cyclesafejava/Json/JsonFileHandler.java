package com.example.cyclesafejava.Json;

import android.util.JsonReader;

import androidx.annotation.RequiresPermission;

import com.example.cyclesafejava.Logger;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;
import com.google.gson.Gson;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class JsonFileHandler {

    private static String SettingsFileName = "Settings.json";
    private static String StatisticsFileName = "Statistics.json";

    public static void writeStatistics(Statistics statistics, File root){
        Gson gson = new Gson();
        String settingsString = gson.toJson(statistics);
        try{
            File file = new File(root.getPath() + "//" + StatisticsFileName);
            if(!file.createNewFile()){
                Logger.debug("Failed");
            }
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(settingsString);
            fileWriter.flush();
            fileWriter.close();
        }
        catch(Exception e){
            Logger.debug(e.getMessage());
        }
    }

    public static void writeSettings(Settings settings, File root){
        Gson gson = new Gson();
        String settingsString = gson.toJson(settings);
        try{
            File file = new File(root.getPath() + "//" + SettingsFileName);
            if(!file.createNewFile()){
                Logger.debug("Failed");
            }
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(settingsString);
            fileWriter.flush();
            fileWriter.close();
        }
        catch(Exception e){
            Logger.debug(e.getMessage());

        }
    }

    public static Statistics readStatistics(String directory){
        Path path = Paths.get(directory, StatisticsFileName);
        if(Files.exists(path)){
            String content = ReadFile(path.toString());
            Statistics statistics = JsonParser.ParseStatistics(content);
            return statistics;
        }
        else {
            File statisticsFile = new File(path.toString());
            return new Statistics(10.5, 15.2, 105.17);
        }
    }

    public static Settings readSettings(String directory){
        Path path = Paths.get(directory, SettingsFileName);
        if(Files.exists(path)){
            String content = ReadFile(path.toString());
            Settings settings = JsonParser.ParseSettings(content);
            return settings;
        }
        else{
            File statisticsFile = new File(path.toString());
            return new Settings();
        }
    }

    private static String ReadFile(String path){
        try{
            String content = new String(Files.readAllBytes(Paths.get(path)));
            return content;
        }
        catch (Exception e){
            return "";
        }
    }


}
