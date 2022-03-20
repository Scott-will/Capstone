package com.example.cyclesafejava.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import com.example.cyclesafejava.Logger;
import com.example.cyclesafejava.data.Events.Event;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothServer extends Thread {
    private BluetoothSocket socket;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    final String BatteryNotif = "Battery Low";
    private String DeviceId = "DSD TECH HC-05";

    @Override
    public void run() {
        listen();
    }

    public void SetDeviceID(String ID){
        this.DeviceId = ID;
    }

    private void listen() {
        while (1 != 2) {
            try {
                InputStream instream = socket.getInputStream();
                byte[] buffer = new byte[1];
                int bytes = instream.read(buffer);
                String readed = new String(buffer, 0, bytes);
                if (readed.equals("B")) {
                    //broadcast();
                }
                //readMessage.append(readed);
            } catch (Exception e) {
                Logger.debug(e.getMessage());
            }
        }
    }

    private void Broadcast(){
        EventBus.getDefault().post(new Event(0, Event.BATTERY));
    }

    public boolean GetDevice()
    {
        Logger.debug("Devices:");
        for(BluetoothDevice d : adapter.getBondedDevices())
        {
            Logger.debug(d.getName());
            if(d.getName().equals(DeviceId)) {
                this.device = d;

            }

        }
        //device = adapter.getRemoteDevice(device.getAddress());
        if (this.device == null)
        {
            return false;
        }
        if (!device.fetchUuidsWithSdp())
        {
            Logger.error("Failed to find UUIDs");
        }

        return true;
    }

    public boolean GetAdapter()
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

    public void Send(byte value){
        try{
            OutputStream outStream = socket.getOutputStream();
            outStream.write(value);
        }
        catch(Exception e){

        }
    }

    public boolean InitializeSocket() throws IOException {
        socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        if (!socket.isConnected())
        {
            try
            {
                //serverSocket = socket;
                if(this.device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC){
                    socket.connect();
                    Logger.debug("Socket Connected");
                }
            }
            catch (Exception e)
            {
                Logger.error(e.toString());
                return false;
            }
        }
        return true;
    }

}
