package gpigb.report;
import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/* should probably be a sensor meh */
public class SystemStatsReporter implements Reporter {
	
	@Override
	public void setID(int newID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getConfigurationStepNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setConfig(Map<String, ConfigurationValue> newConfig,
			ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr,
			ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void generateReport(List<RecordSet<?>> data) {
		/* Interface java.lang.management.OperatingSystemMXBean defines only few methods, 
		 * while real implementation of this interface in JRE usually have much more methods, 
		 * so use reflection to discover them at run time. 
		 */
		OperatingSystemMXBean operatingSystemMXBean =  ManagementFactory.getOperatingSystemMXBean();
		/* Iterate over all public non-static methods that:
		 * 1) have name starting with "get" and 
		 * 2) are defined in some class that implements OperatingSystemMXBean interface (helps avoid non-relevent methods that start with get)
		 */
			for (Method method: operatingSystemMXBean.getClass ().getMethods ()) 
			{
			    method.setAccessible (true);
			    String methodName = method.getName ();
			    if (methodName.startsWith ("get")
			        && Modifier.isPublic (method.getModifiers ())
			        && OperatingSystemMXBean.class.isAssignableFrom (
			            method.getDeclaringClass ())) {
			        try
			        {
			            System.out.println (
			                methodName.substring (3) + ": " + 
			                method.invoke (operatingSystemMXBean));
			        }
			        catch (Throwable ex)
			        {
			            // Catch a flu
			        }
			    }
			}
		
	}
	
	/* test run */
	public static void main (String[] args) {	
		SystemStatsReporter ssr = new SystemStatsReporter();
		ssr.generateReport(null);
	}
}
