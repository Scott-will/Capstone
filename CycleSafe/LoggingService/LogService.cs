using NLog;
using NLog.Config;
using System.Reflection;
using NLog;
using NLog.Config;
using System.Xml;
using System.IO;
using System.Linq;
using System;

namespace LoggingService
{
    public class LogService : ILogService
    {
        private Logger logger;

        public void Initialize(Assembly assembly, string assemblyName)
        {
            //var location = $"{assemblyName}\\Assets.NLog.config"; 
            var nlogConfigFile = GetEmbeddedResourceStream(assembly, "NLog.config");
            if (nlogConfigFile != null)
            {
                var xmlReader = System.Xml.XmlReader.Create(nlogConfigFile);
                NLog.LogManager.Configuration = new XmlLoggingConfiguration(xmlReader, null);
            }
            this.logger = LogManager.GetCurrentClassLogger();
        }

        public void CollectLogs()
        {
            //find the log files
            //push them to some where (my computer or http server)

        }

        public void Debug(string message)
        {
            this.logger.Info(message);
        }

        public void Error(string message)
        {
            this.logger.Error(message);
        }
        public static Stream GetEmbeddedResourceStream(Assembly assembly, string resourceFileName)
        {
            var resourcePaths = assembly.GetManifestResourceNames()
              .Where(x => x.EndsWith(resourceFileName, StringComparison.OrdinalIgnoreCase))
              .ToList();
            if (resourcePaths.Count == 1)
            {
                return assembly.GetManifestResourceStream(resourcePaths.Single());
            }
            return null;
        }
    }     
}
