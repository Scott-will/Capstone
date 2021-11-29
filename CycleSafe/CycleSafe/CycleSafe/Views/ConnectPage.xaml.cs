﻿using LoggingService;
using System;
using System.Threading.Tasks;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CycleSafe.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class ConnectPage : ContentPage
    {
        private IBluetoothHandler handler;

        private readonly ILogService Log;
        public ConnectPage()
        {
            this.Log = DependencyService.Get<ILogService>(DependencyFetchTarget.GlobalInstance);
            this.handler = DependencyService.Get<IBluetoothHandler>(DependencyFetchTarget.GlobalInstance);
            InitializeComponent();
            Routing.RegisterRoute(nameof(HomeScreen), typeof(HomeScreen));
        }

        async void Connect(object sender, EventArgs e)
        {
            var path = $"App folder path :{Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData)}";
            Log.Debug("Button was pushed!");
            await handler.Initialize();
            Task.Run(async () =>
            {
                handler.Listen();
            });
            await Shell.Current.GoToAsync(nameof(HomeScreen));
        }
    }
}