package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.IDGenerator;
import gpigb.classloading.JarFileComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.configuration.handlers.GUIConfigHandler;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A Real Time graph which can be used to plot sensor data
 */
public class ReporterPlotRTSmart implements Reporter
{
	String title;
	Integer height;
	Integer width;
	SmartGrapher grapher;
	private int id;

	public ReporterPlotRTSmart()
	{
	}

	@Override
	public void generateReport(List<RecordSet<?>> dataStream)
	{
		for(RecordSet<?> recordSet : dataStream) {
			int data = (Integer) recordSet.getDataAtPosition(0).getData();
			grapher.plot(data);
		}
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Title", new ConfigurationValue(ValueType.String, "Real Time Graph"));
		configMap.put("Width", new ConfigurationValue(ValueType.Integer, new Integer(900)));
		configMap.put("Height", new ConfigurationValue(ValueType.Integer, new Integer(500)));
		configMap.put("AxisLabel", new ConfigurationValue(ValueType.String, "Default Label"));
		return configMap;
	}
	
	@Override
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			title = (String) newSpec.get("Title").strValue;
			width = ((Integer)newSpec.get("Width").intValue).intValue();
			height = ((Integer)newSpec.get("Height").intValue).intValue();
			System.out.println("Title: " + title + " Width: " + width + " Height: " + height);
			grapher = new SmartGrapher(title, width, height);
			grapher.setLabel((String) newSpec.get("AxisLabel").strValue);
			
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public String toString()
	{
		return "Smart Plotter: " + title;
	}

	
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

	/* test the configures */
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
				
			Integer gReproterID = rMgr.getModuleIDByName("gpigb.report.ReporterPlotRTSmart");
			Reporter gReporter = (Reporter) rMgr.getObjectByID(rMgr.createObjectOfModule(gReproterID)).get();
			
			Integer sPort1ID = seMgr.getModuleIDByName("gpigb.sense.RandomValueSensor");
			Sensor<Integer> s1 = (Sensor<Integer>) seMgr.getObjectByID(seMgr.createObjectOfModule(sPort1ID)).get();
	
			GUIConfigHandler configHandler = new GUIConfigHandler(aMgr.getAvailableObjects(), rMgr.getAvailableObjects(), stMgr.getAvailableObjects(), seMgr.getAvailableObjects());
			Map<String, ConfigurationValue> config;
			
			config = gReporter.getConfigSpec();
			configHandler.getConfiguration(config);
			gReporter.setConfig(config, null, rMgr, null, null);
	}
}
