package com.example.cyclesafejava.ViewModels;

import android.app.Activity;

import com.example.cyclesafejava.Bluetooth.BluetoothService;

public class BluetoothViewModel {

    private BluetoothService handler;

    public BluetoothViewModel(BluetoothService bluetoothService){
        this.handler = bluetoothService;
    }

    public boolean Initialize(){
        try{
            if(this.handler.Initialize()){
                return true;
            }
            return false;

        }
        catch(Exception e){
            return false;
        }

    }

    public void StartListening(){
        this.handler.StartListening();
    }

    public void SetDeviceId(String Id){
        this.handler.SetDeviceID(Id);
    }

    public void SendSettings(int component){
        Integer componentValue = new Integer(component);
        this.handler.Send(componentValue.byteValue());
    }
}
