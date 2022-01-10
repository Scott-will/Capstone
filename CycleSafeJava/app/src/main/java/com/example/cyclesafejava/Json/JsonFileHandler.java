package com.example.cyclesafejava.Json;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;
import org.json.JSONObject;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
public class JsonFileHandler {

    public static void writeStatistics(Statistics statistics){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("FastestSpeed", statistics.FastestSpeed);
            jsonObject.put("LongestRide",statistics.LongestRide);
            jsonObject.put("TotalDistance",statistics.TotalDistance);
            WriteFile(jsonObject);
        }
        catch(Exception e){

        }

    }

    public static void writeSettings(Settings settings){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("DeviceID", settings.DeviceID);
            WriteFile(jsonObject);
        }
        catch(Exception e){

        }
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

    private static boolean WriteFile(JSONObject json){
        try{
            FileWriter file = new FileWriter("");
            file.write(json.toString());
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
