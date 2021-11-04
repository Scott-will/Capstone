using LoggingService;
using System;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CycleSafe.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class ConnectPage : ContentPage
    {

        private readonly ILogService Log;
        public ConnectPage()
        {
            this.Log = DependencyService.Get<ILogService>(DependencyFetchTarget.GlobalInstance);
            InitializeComponent();
            Routing.RegisterRoute(nameof(HomeScreen), typeof(HomeScreen));
        }

        async void Connect(object sender, EventArgs e)
        {
            var path = $"App folder path :{Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData)}";
            Log.Debug("Button was pushed!");
            await Shell.Current.GoToAsync(nameof(HomeScreen));
        }
    }
}