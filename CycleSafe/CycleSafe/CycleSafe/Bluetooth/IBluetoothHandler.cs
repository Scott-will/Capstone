using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace CycleSafe.Views
{
    public interface IBluetoothHandler
    {
        Task<bool> Initialize();
    }
}
