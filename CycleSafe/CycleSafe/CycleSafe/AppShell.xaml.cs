using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CycleSafe
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AppShell : Shell
    {
        public AppShell()
        {
            InitializeComponent();
            Routing.RegisterRoute(nameof(ConnectPage), typeof(ConnectPage));

        }
    }
}