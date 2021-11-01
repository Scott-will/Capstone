using System;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CycleSafe.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class ConnectPage : ContentPage
    {
        public ConnectPage()
        {
            InitializeComponent();
            Routing.RegisterRoute(nameof(HomeScreen), typeof(HomeScreen));
        }

        async void Connect(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync(nameof(HomeScreen));
        }
    }
}