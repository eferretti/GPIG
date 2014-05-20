package gpigb.sense;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.IDGenerator;
import gpigb.classloading.JarFileComponentManager;
import gpigb.configuration.Configurable;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.configuration.handlers.GUIConfigHandler;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.store.Store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinuxProcessCPUSensor implements Sensor<Float>, Configurable
{
	File statFile = null;
	int pid = 21794;
	List<SensorObserver> observers = new ArrayList<>();
	Float averageUsage = null;
	Float dampingFactor = 0.5f;
	
	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> spec = new HashMap<>();
		spec.put("Process ID", new ConfigurationValue(ValueType.Integer, pid));
		spec.put("Damping Factor", new ConfigurationValue(ValueType.Float, dampingFactor));
		return spec;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			pid = newSpec.get("Process ID").intValue;
			statFile = new File("/proc/" + pid + "/stat");
			dampingFactor = newSpec.get("Damping Factor").fltValue;
			averageUsage = null;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private int id;
	@Override
	public int getID()
	{
		return id;
	}

	@Override
	public Float poll()
	{
	    try {
	        RandomAccessFile reader = new RandomAccessFile("/proc/" + pid + "/stat", "r");
	        String load = reader.readLine();
	        String[] toks = load.split(" ");

	        long sutime = Long.parseLong(toks[13]);
	        long sstime = Long.parseLong(toks[14]);
	        long scutime = Long.parseLong(toks[15]);
	        long scstime = Long.parseLong(toks[16]);
	        
	        reader.close();
	        reader = new RandomAccessFile("/proc/stat", "r");
	        toks = reader.readLine().split(" ");
	        reader.close();
	        long time1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[5])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
	        
	        try
	        {
	        	Thread.sleep(100);
	        }
	        catch(Exception e)
	        {
	        	
	        }
	        
	        reader.close();
	        reader = new RandomAccessFile("/proc/" + pid + "/stat", "r");
	        load = reader.readLine();
	        reader.close();
	        toks = load.split(" ");

	        long eutime = Long.parseLong(toks[13]);
	        long estime = Long.parseLong(toks[14]);
	        long ecutime = Long.parseLong(toks[15]);
	        long ecstime = Long.parseLong(toks[16]);
	        reader.close();
	        reader = new RandomAccessFile("/proc/stat", "r");
	        toks = reader.readLine().split(" ");
	        reader.close();
	        long time2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[5])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
	        
	        long dutime = eutime - sutime;
	        long dstime = estime - sstime;
	        long dcutime = ecutime - scutime;
	        long dcstime = ecstime - scstime;
	        long dt = time2 - time1;
	        
	        Float instantaniousUsage = 100*((dutime + dstime + dcutime + dcstime)/(1.0f*dt));

	        if(averageUsage == null)
	        {
	        	averageUsage = instantaniousUsage;
	        }
	        else
	        {
	        	averageUsage = (dampingFactor * averageUsage) + ((1-dampingFactor) * instantaniousUsage);
	        }
	        
	        //System.out.println("Reading: " + averageUsage);
	        notifyObservers();
	        return averageUsage;
	        
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	    notifyObservers();
	    return 0f;
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void registerObserver(SensorObserver obs)
	{
		if(!observers.contains(obs))
			observers.add(obs);
	}

	@Override
	public void removeObserver(SensorObserver obs)
	{
		observers.remove(obs);
	}

	@Override
	public void notifyObservers() {
		for(SensorObserver obs : observers)
			obs.update(id, averageUsage.intValue());
	}

	@Override
	public void setID(int newID) {
		this.id = newID;
	}
	
	@Override
	public int getConfigurationStepNumber() {
		return 1;
	}
	
	public static void main(String[] args) {
		IDGenerator.setMinID(47);
		
		JarFileComponentManager<Analyser> aMgr = new JarFileComponentManager<>(Analyser.class);
		JarFileComponentManager<Reporter> rMgr = new JarFileComponentManager<>(Reporter.class);
		JarFileComponentManager<Sensor> seMgr = new JarFileComponentManager<>(Sensor.class);
		JarFileComponentManager<Store> stMgr = new JarFileComponentManager<>(Store.class);
		
		aMgr.addModuleDirectory("~/HUMS_Modules");
		rMgr.addModuleDirectory("~/HUMS_Modules");
		seMgr.addModuleDirectory("~/HUMS_Modules");
		stMgr.addModuleDirectory("~/HUMS_Modules");
		
		aMgr.refreshModules();
		rMgr.refreshModules();
		seMgr.refreshModules();
		stMgr.refreshModules();
			
		Integer cpuSensID = seMgr.getModuleIDByName("gpigb.sense.LinuxProcessCPUSensor");
		Sensor<Double> cpuSens = (Sensor<Double>) seMgr.getObjectByID(seMgr.createObjectOfModule(cpuSensID)).get();
		
		GUIConfigHandler configHandler = new GUIConfigHandler(aMgr.getAvailableObjects(), rMgr.getAvailableObjects(), stMgr.getAvailableObjects(), seMgr.getAvailableObjects());
		
		Map<String, ConfigurationValue> config;		
		config = cpuSens.getConfigSpec();
		configHandler.getConfiguration(config);
		cpuSens.setConfig(config, null, null, null, null);
		
		while(true) {
			System.out.println(cpuSens.poll());
		}
	}
}
