package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reports a Sensor that has a value out of range, it reports the Sensor, time
 * and value.
 */
public class OutOfRangeReporter implements Reporter
{
	PrintStream outputStream;
	
	public void generateReport(List<RecordSet<?>> EroniousData)
	{
		if (EroniousData.isEmpty()) return;

		String ErrorMessage = "SensorID: " + EroniousData.get(0).getSensorID() + " Time: "
				+ EroniousData.get(0).getDataAtPosition(0).getTimestamp() + " Reading: "
				+ EroniousData.get(0).getDataAtPosition(0).getData() + "\n";
		outputStream.print(ErrorMessage);
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		return null;
//		HashMap<String, ConfigurationValue> config = new HashMap<>();
//		//config.put("PrintStream", new ConfigurationValue(ValueType.OutStream, null));
//		return config;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.outputStream = System.out;//(PrintStream) newSpec.get("PrintStream").value;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
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
