using System;
using System.Text;
using Android.Bluetooth;
using System.Linq;
using Xamarin.Forms;
using LoggingService;
using System.Threading.Tasks;
using CycleSafe.Views;
using Java.Util;

namespace CycleSafe.Bluetooth
{
    public class BluetoothHandler : IBluetoothHandler
    {

        private BluetoothAdapter adapter;
        private BluetoothDevice device;
        private const string Name = "The jacket H2O";//"raspberrypi";
        private readonly ILogService Log;
        private BluetoothSocket socket;
        private BluetoothServerSocket serverSocket;

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
            //adapter.CancelDiscovery();
            if (!InitializeSocket())
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
                var type = d.Type.ToString();
                Log.Debug($"{type}");
            }
            device = (from bd in adapter.BondedDevices where bd.Name == Name select bd).FirstOrDefault();
            device = adapter.GetRemoteDevice(device.Address);
            if (device == null)
            {
                return false;
            }
            if (!device.FetchUuidsWithSdp())
            {
                Log.Error("Failed to find UUIDs");
            }
            //UUID uuid = device.GetUuids().FirstOrDefault();

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

        private bool InitializeSocket()
        {
            //socket = new BluetoothServerSocket();
            socket = device.CreateInsecureRfcommSocketToServiceRecord(UUID.FromString("00001101-0000-1000-8000-00805F9B34FB"));
            if (!socket.IsConnected)
            {
                try
                {
                    //serverSocket = socket;
                    socket.Connect();               
                }
                catch (Exception e)
                {
                    Log.Error($"{e}");
                    return false;
                }
            }
            
                   
            return true;
        }

        public async Task Listen()
        {
            var listening = true;
            Log.Debug("Listening");
            var instream = socket.InputStream;
            byte[] uintBuffer = new byte[sizeof(uint)];
            byte[] textBuffer;

            while (listening)
            {
                try
                {
                    await instream.ReadAsync(uintBuffer, 0, uintBuffer.Length);
                    var readLength = BitConverter.ToUInt32(uintBuffer, 0);

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
    }
    //https://developer.android.com/guide/topics/connectivity/bluetooth/permissions
}
