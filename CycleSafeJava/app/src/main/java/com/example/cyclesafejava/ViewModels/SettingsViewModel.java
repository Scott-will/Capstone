package com.example.cyclesafejava.ViewModels;

import android.content.Context;

import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.Services.SettingsService;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;

public class SettingsViewModel {
    private SettingsService settingsService;

    public SettingsViewModel(SettingsService settingsService){
        this.settingsService = settingsService;
    }

    public void SaveSettings(Context context, Settings settings){
        this.settingsService.Save(context, settings);
    }
    public void SetDeviceId(Context context, Settings settings, String Id){
        settings.DeviceID = Id;
        this.settingsService.Save(context, settings);
    }

    public boolean BrakeSwitch(Settings settings){
        settings.Brake = !settings.Brake;
        return settings.Brake;

    }

    public boolean BatteryNotifSwitch(Settings settings){
        settings.BatteryNotif = !settings.BatteryNotif;
        return settings.BatteryNotif;
    }

    public boolean TurnSwitch(Settings settings){
        settings.Turn = !settings.Turn;
        return settings.Turn;
    }

    public Settings LoadSettings(Context context){
        Settings settings = this.settingsService.LoadData(context.getFilesDir().toString());
        return settings;

    }

}
