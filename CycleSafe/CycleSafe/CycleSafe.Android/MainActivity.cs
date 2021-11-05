using Android.App;
using Android.Content.PM;
using Android.Runtime;
using Android.OS;
using LoggingService;
using Xamarin.Forms;
using CycleSafe.Bluetooth;
using CycleSafe.Views;

namespace CycleSafe.Droid
{
    [Activity(Label = "CycleSafe", Icon = "@mipmap/icon", Theme = "@style/MainTheme", MainLauncher = true, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation | ConfigChanges.UiMode | ConfigChanges.ScreenLayout | ConfigChanges.SmallestScreenSize )]
    public class MainActivity : global::Xamarin.Forms.Platform.Android.FormsAppCompatActivity
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);

            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            global::Xamarin.Forms.Forms.Init(this, savedInstanceState);
            DependencyService.Register<IBluetoothHandler, BluetoothHandler>();
            LoadApplication(new App());

            this.Bootstraping();
        }
        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        private void Bootstraping()
        {
            var assembly = this.GetType().Assembly;
            var assemblyName = assembly.GetName().Name;
            DependencyService.Get<ILogService>().Initialize(assembly, assemblyName);
        }
    }
}