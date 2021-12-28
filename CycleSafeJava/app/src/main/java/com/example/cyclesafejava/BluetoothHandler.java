package com.example.cyclesafejava;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;


public class BluetoothHandler {
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private final String Name = "DSD TECH HC-05";//"arduino";
    private BluetoothSocket socket;
    private BluetoothServerSocket serverSocket;

    public BluetoothHandler(){

    }
    public boolean Initialize() throws IOException {
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

        return true;

    }

    private boolean GetDevice()
    {
        //Log.Debug("Devices:");
        for(BluetoothDevice d : adapter.getBondedDevices())
        {
            if(d.getName() == Name) {
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
            //Log.Error("Failed to find UUIDs");
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
            //Log.Error("Adapter is not enabled");
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
                //Log.Error($"{e}");
                return false;
            }
        }


        return true;
    }

    /*public async Task Listen()
    {
        boolean listening = true;
        //Log.Debug("Listening");
        InputStream instream = socket.getInputStream();
        byte[] uintBuffer = new byte[(UInt)];
        byte[] textBuffer;

        while (listening)
        {
            try
            {
                instream.read(uintBuffer, 0, uintBuffer.length);
                //var readLength = B.ToUInt32(uintBuffer, 0);

                textBuffer = new byte[readLength];
                await instream.ReadAsync(textBuffer, 0, (int)readLength);

                var message = Encoding.UTF8.GetString(textBuffer);
                Log.Debug($"Recieved message:\n{message}");
                Alerts.AlertService.Alert(message);
            }
            catch(Exception e)
            {
                Log.Error(e.Message);
                listening = false;
                break;
            }
        }

        Log.Debug("Stop listening");
    }

    public void Send()
    {
        var outStream = socket.OutputStream;
        outStream.WriteByte(1);
    }*/
}


