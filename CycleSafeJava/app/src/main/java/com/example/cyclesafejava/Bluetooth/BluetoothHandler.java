package com.example.cyclesafejava.Bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.example.cyclesafejava.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;


public class BluetoothHandler {
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private String DeviceId = "WH-1000XM3";// TECH HC-05";//"arduino";XM3
    private final String intentMessage = "This app requires bluetooth permissions to conenct to the Arduino";
    private BluetoothSocket socket;
    private BluetoothServerSocket serverSocket;
    private Context context;
    private Activity activity;
    private boolean BluetoothEnabled;

    public BluetoothHandler(Context context, Activity activity, boolean bluetoothEnabled){
        this.context = context;
        this.activity =  activity;
        this.BluetoothEnabled = bluetoothEnabled;
    }

    public void SetDeviceID(String ID){
        this.DeviceId = ID;
    }

    public boolean Initialize() throws IOException {
        if(!BluetoothEnabled){
            //Do nothing
            return false;
        }
        if (!GetAdapter())
        {
            return false;
        }

        if (!GetDevice())
        {
            //Log.Error("Could not find device");
            return false;
        }
        //adapter.CancelDiscovery();
        if (!InitializeSocket())
        {
            //Log.Error("Could not initialize socket");
            return false;
        }
        Listen listener = new Listen();
        listener.Listen(socket);
        return true;

    }

    private boolean GetDevice()
    {
        Logger.debug("Devices:");
        for(BluetoothDevice d : adapter.getBondedDevices())
        {
            Logger.debug(d.getName());
            if(d.getName() == DeviceId) {
                device = d;

            }

        }
        //device = adapter.getRemoteDevice(device.getAddress());
        if (device == null)
        {
            return false;
        }
        if (!device.fetchUuidsWithSdp())
        {
            Logger.error("Failed to find UUIDs");
        }

        return true;
    }

    private boolean GetAdapter()
    {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null)
        {
            return false;
        }
        if (!adapter.isEnabled())
        {
            Logger.error("Adapter is not enabled");
            return false;
        }
        return true;
    }

    private boolean InitializeSocket() throws IOException {
        socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        if (!socket.isConnected())
        {
            try
            {
                //serverSocket = socket;
                socket.connect();
            }
            catch (Exception e)
            {
                Logger.error(e.toString());
                return false;
            }
        }


        return true;
    }

    public class Listen extends Thread{
        public void Listen(BluetoothSocket socket){
            boolean listening = true;
            //Log.Debug("Listening");
            try {
                InputStream instream = socket.getInputStream();

                byte[] buffer = new byte[1];
                int bytes;

                while (1 != 2) {
                    try {

                        bytes = instream.read(buffer);
                        String readed = new String(buffer, 0, bytes);

                        //readMessage.append(readed);
                    } catch (Exception e) {

                    }
                }
            }
            catch(Exception e){

            }
            Logger.debug("Stop listening");
        }
    }
}


