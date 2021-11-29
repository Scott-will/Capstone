using Xamarin.Forms;

namespace CycleSafe.Alerts
{
    public class AlertService
    {
        
        public static void Alert(string message)
        {
            App.Current.MainPage.DisplayAlert("alert", message, "OK");
        }
    }
}
