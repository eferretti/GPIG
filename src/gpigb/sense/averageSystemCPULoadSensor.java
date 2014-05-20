package gpigb.sense;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.Configurable;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.store.Store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class averageSystemCPULoadSensor implements Sensor<Double>, Configurable
{
	OperatingSystemMXBean operatingSystemMXBean =  ManagementFactory.getOperatingSystemMXBean();
	List<SensorObserver> observers = new ArrayList<>();
	Double averageUsage = null;
	
	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> spec = new HashMap<>();
		return spec;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
			return true;
	}

	private int id;
	@Override
	public int getID()
	{
		return id;
	}

	@Override
	public Double poll()
	{
		averageUsage = operatingSystemMXBean.getSystemLoadAverage() * 100;
		System.out.println("Average CPU load: " + averageUsage); 
		notifyObservers();
		return averageUsage;
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
	public void notifyObservers()
	{
		for(SensorObserver obs : observers)
			obs.update(id, averageUsage.intValue());
	}

	@Override
	public void setID(int newID)
	{
		this.id = newID;
	}
	
	@Override
	public int getConfigurationStepNumber() {
		
		return 1;
	}
	
	/* test run */
	public static void main(String[] args) {
		averageSystemCPULoadSensor sens = new averageSystemCPULoadSensor();
		while(true) {
			System.out.println(sens.poll());
		}
	}
}
