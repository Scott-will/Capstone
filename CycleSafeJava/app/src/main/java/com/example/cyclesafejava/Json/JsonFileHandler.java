package com.example.cyclesafejava.Json;

import android.util.JsonReader;

import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class JsonFileHandler {

    public static void writeStatistics(){

    }

    public static void writeSettings(){

    }

    public static Statistics readStatistics(){
        String content = ReadFile("");
        Statistics statistics = JsonParser.ParseStatistics(content);
        return statistics;

    }

    public static Settings readSettings(){
        String content = ReadFile("");
        Settings settings = JsonParser.ParseSettings(content);
        return settings;
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
