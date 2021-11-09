using CycleSafe.Views;
using LoggingService;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CycleSafe
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class HomeScreen : ContentPage
    {
        private IBluetoothHandler handler;
        private readonly ILogService Log;
        public HomeScreen()
        {
            this.Log = DependencyService.Get<ILogService>(DependencyFetchTarget.GlobalInstance);
            this.handler = DependencyService.Get<IBluetoothHandler>(DependencyFetchTarget.GlobalInstance);
            InitializeComponent();
        }
    }
}