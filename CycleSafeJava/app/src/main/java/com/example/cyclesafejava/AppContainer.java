package com.example.cyclesafejava;

import com.example.cyclesafejava.Bluetooth.BluetoothService;
import com.example.cyclesafejava.Services.MapsService;
import com.example.cyclesafejava.Services.SettingsService;
import com.example.cyclesafejava.Services.StatisticsService;

public class AppContainer {
    //need all classes here
    public BluetoothService bluetoothService = new BluetoothService();
    public SettingsService settingsService = new SettingsService();
    public StatisticsService statisticsService = new StatisticsService();
    public MapsService mapsService = new MapsService();

}
