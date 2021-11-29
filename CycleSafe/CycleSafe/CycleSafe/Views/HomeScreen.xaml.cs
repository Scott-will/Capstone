using CycleSafe.Views;
using CycleSafe.Alerts;
using LoggingService;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using System;

namespace CycleSafe.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class HomeScreen : ContentPage
    {
        private IBluetoothHandler handler;
        private readonly ILogService Log;
        public HomeScreen()
        {
            Log = DependencyService.Get<ILogService>(DependencyFetchTarget.GlobalInstance);
            handler = DependencyService.Get<IBluetoothHandler>(DependencyFetchTarget.GlobalInstance);
            InitializeComponent();
        }

        public void Alert(object sender, EventArgs e)
        {
            Log.Debug("pressed");
        }
    }
}