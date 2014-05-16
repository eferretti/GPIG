package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Real Time graph which can be used to plot sensor data
 */
public class ReporterPlotRTSmart implements Reporter
{
	String title;
	SmartGrapher grapher;

	public ReporterPlotRTSmart()
	{
	}

	@Override
	public void generateReport(List<RecordSet<?>> dataStream)
	{
		RecordSet<?> RecordSet = dataStream.get(0);
		int data = (Integer) RecordSet.getDataAtPosition(0).getData();
		grapher.plot(data);
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Title", new ConfigurationValue(ValueType.String, "Real Time Graph"));
		configMap.put("Width", new ConfigurationValue(ValueType.Integer, new Integer(900)));
		configMap.put("Height", new ConfigurationValue(ValueType.Integer, new Integer(500)));
		
		return configMap;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.title = (String) newSpec.get("Title").strValue;
			this.grapher = new SmartGrapher(title, ((Integer)newSpec.get("Width").intValue).intValue(), ((Integer)newSpec.get("Height").intValue).intValue());
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public String toString()
	{
		return "Smart Plotter: " + title;
	}

	private int id;
	public void setID(int newID)
	{
		this.id = newID;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	@Override
	public int getConfigurationStepNumber() {
		
		return 1;
	}
}
