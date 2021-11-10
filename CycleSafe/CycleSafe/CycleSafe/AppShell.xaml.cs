﻿using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CycleSafe
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AppShell : Shell
    {
        public AppShell()
        {
            InitializeComponent();
            Routing.RegisterRoute(nameof(Views.ConnectPage), typeof(Views.ConnectPage));
            Routing.RegisterRoute(nameof(Views.HomeScreen), typeof(Views.HomeScreen));

        }
    }
}