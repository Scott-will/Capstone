using System;
using Xamarin.Forms;

namespace CycleSafe
{
    public partial class AppShell : Xamarin.Forms.ContentPage
    {
        public AppShell()
        {
            InitializeComponent();
            Routing.RegisterRoute(nameof(ConnectPage), typeof(ConnectPage));
        }

        async void Connect(object sender, EventArgs e)
        {
            try
            {
                var route = $"{nameof(ConnectPage)}";
                await Shell.Current.GoToAsync(route);
            }

            catch(Exception error)
            {
                Console.WriteLine(error);
            }
           
        }
    }
}