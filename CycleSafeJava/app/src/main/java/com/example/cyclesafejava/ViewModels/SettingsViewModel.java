package com.example.cyclesafejava.ViewModels;

import android.content.Context;

import com.example.cyclesafejava.Json.JsonFileHandler;
import com.example.cyclesafejava.Services.SettingsService;
import com.example.cyclesafejava.data.Settings;
import com.example.cyclesafejava.data.Statistics;

public class SettingsViewModel {
    private SettingsService settingsService;
    private Settings settings;

    public SettingsViewModel(SettingsService settingsService){
        this.settingsService = settingsService;
    }

    public void SetDeviceId(Context context, String Id){
        this.settings.DeviceID = Id;
        this.settingsService.Save(context, this.settings);
    }

    public boolean BrakeSwitch(){
        this.settings.Brake = !this.settings.Brake;
        return this.settings.Brake;

    }

    public boolean BatteryNotifSwitch(){
        this.settings.BatteryNotif = !this.settings.BatteryNotif;
        return this.settings.BatteryNotif;
    }

    public boolean TurnSwitch(){
        this.settings.Turn = !this.settings.Turn;
        return this.settings.Turn;
    }

    public Settings LoadSettings(Context context){
        this.settings = this.settingsService.LoadData(context.getPackageResourcePath());
        return this.settings;

    }

}
