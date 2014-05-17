package gpigb.report;
import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.io.File;
import java.util.List;
import java.util.Map;
/* should probably be a sensor meh */
public class JVMStatsReporter implements Reporter{
  

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
	 /* The total number of processors or cores currently accessible to the JVM */
    System.out.println("Accessible processors (cores): " + 
        Runtime.getRuntime().availableProcessors());

    /* The total amount of free memory currently accessible to the JVM */
    System.out.println("Free memory (bytes): " + 
        Runtime.getRuntime().freeMemory());

    /* This will return Long.MAX_VALUE if there is no preset limit */
    long maxMemory = Runtime.getRuntime().maxMemory();
    /* Maximum amount of memory the JVM will attempt to use */
    System.out.println("Maximum memory (bytes): " + 
        (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

    /* Total memory currently in use by the JVM */
    System.out.println("Total memory (bytes): " + 
        Runtime.getRuntime().totalMemory());

    /* Get a list of all filesystem roots on this system */
    File[] roots = File.listRoots();

    /* For each filesystem root, print some info */
    for (File root : roots) {
      System.out.println("File system root: " + root.getAbsolutePath());
      System.out.println("Total space (bytes): " + root.getTotalSpace());
      System.out.println("Free space (bytes): " + root.getFreeSpace());
      System.out.println("Usable space (bytes): " + root.getUsableSpace());
    }
}


/* test run */
public static void main(String[] args) {
	JVMStatsReporter msr = new JVMStatsReporter();
	msr.generateReport(null);
  }
}