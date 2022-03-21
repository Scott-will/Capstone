package com.example.cyclesafejava.Bluetooth;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.cyclesafejava.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BluetoothService extends Service {

    private boolean BluetoothEnabled = true;
    public boolean receivedAlert = false;
    public BluetoothServer bluetoothServer = new BluetoothServer();

    public void SetDeviceID(String ID){
        this.bluetoothServer.SetDeviceID(ID);
    }

    public void Send(byte byteValue){
        this.bluetoothServer.Send(byteValue);
    }

    public boolean Initialize() throws IOException {
        if(!BluetoothEnabled){
            //Do nothing
            return false;
        }
        if (!bluetoothServer.GetAdapter())
        {
            return false;
        }

        if (!bluetoothServer.GetDevice())
        {
            //Log.Error("Could not find device");
            return false;
        }
        //adapter.CancelDiscovery();
        if (!bluetoothServer.InitializeSocket())
        {
            //Log.Error("Could not initialize socket");
            return false;
        }
        return true;
    }

    public void StartListening(){
        this.bluetoothServer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


