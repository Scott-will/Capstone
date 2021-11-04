using GalaSoft.MvvmLight.Ioc;
using LoggingService;
using System;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CycleSafe
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            DependencyService.Register<ILogService, LogService>();

            MainPage = new AppShell();
        }

        protected override void OnStart()
        {
        }

        protected override void OnSleep()
        {
        }

        protected override void OnResume()
        {
        }
    }
}
