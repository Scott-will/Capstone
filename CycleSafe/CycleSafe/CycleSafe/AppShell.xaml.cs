using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

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