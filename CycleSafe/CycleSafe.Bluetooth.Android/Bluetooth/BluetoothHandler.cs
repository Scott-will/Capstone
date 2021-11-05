using System;
using System.Collections.Generic;
using System.Text;
using Android.Bluetooth;
using System.Linq;
using Xamarin.Forms;
using LoggingService;
using Java.Util;
using System.Threading.Tasks;
using CycleSafe.Views;

namespace CycleSafe.Bluetooth
{
    public class BluetoothHandler : IBluetoothHandler
    {

        private BluetoothAdapter adapter;
        private BluetoothDevice device;
        private const string Name = "Name";
        private readonly ILogService Log;
        private BluetoothSocket socket;

        public BluetoothHandler()
        {
            Log = DependencyService.Get<ILogService>(DependencyFetchTarget.GlobalInstance);
        }
        public async Task<bool> Initialize()
        {
            if (!GetAdapter())
            {
                Log.Error("Failed to initialize adapter");
                return false;
            }

            if (!GetDevice())
            {
                Log.Error("Could not find device");
                return false;
            }

            var socket_init = await InitializeSocket();
            if (!socket_init)
            {
                Log.Error("Could not initialize socket");
                return false;
            }
            return true;

        }

        public bool GetDevice()
        {
            Log.Debug("Devices:");
            foreach(var d in adapter.BondedDevices)
            {
                Log.Debug($"{d.Name}");
            }
            device = (from bd in adapter.BondedDevices where bd.Name == Name select bd).FirstOrDefault();
            if (device == null)
            {
                return false;
            }
            return true;
        }

        private bool GetAdapter()
        {
            adapter = BluetoothAdapter.DefaultAdapter;
            if (adapter == null)
            {
                Log.Error("Could not find adapter");
                return false;
            }
            if (!adapter.IsEnabled)
            {
                Log.Error("Adapter is not enabled");
                return false;
            }
            return true;
        }

        private async Task<bool> InitializeSocket()
        {
            socket = device.CreateRfcommSocketToServiceRecord(UUID.FromString(""));
            try
            {
                await socket.ConnectAsync();
            }
            catch(Exception e)
            {
                Log.Error($"{e}");
                return false;
            }           
            return true;
        }
    }
}
