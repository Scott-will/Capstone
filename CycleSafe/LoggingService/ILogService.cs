using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;

namespace LoggingService
{
    public interface ILogService
    {
        void Initialize(Assembly assembly, string assemblyName);

        void Debug(string message);

        void Error(string message);

        void CollectLogs();

    }
}
