package gpigb.report;

import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.store.Store;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A reporter which is designed to be used with
 * {@link gpigb.analyse.AccelerationAnalyser}
 */
public class UnexpectedAccelerationReporter implements Reporter
{
	PrintStream outputStream;
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateReport(List<RecordSet<?>> data)
	{
		RecordSet<Integer> errorSigniture = (RecordSet<Integer>) data.get(0);
		String errorMessage = "SensorID: " + errorSigniture.getSensorID() + " TimeFrom: "
				+ errorSigniture.getFromTime() + " TimeTo: " + errorSigniture.getToTime() + "\n";

		outputStream.print(errorMessage);
		
		Long timeFrame = errorSigniture.getToTime().getTime() - errorSigniture.getFromTime().getTime();

		Integer valueDifference = errorSigniture.getDataAtPosition(0).getData()
				- errorSigniture.getDataAtPosition(errorSigniture.getRecordCount() - 1).getData();

		errorMessage = "In " + timeFrame + "ms the sensor experianced a change of " + valueDifference + "\n";
		outputStream.println(errorMessage);
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> config = new HashMap<>();
		config.put("PrintStream", new ConfigurationValue(ValueType.OutStream, null));
		return config;
		
//		outputStream = (PrintStream) (config.get("PrintStream") != null ? config.get("PrintStream").value : System.out);
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec)
	{
		try
		{
			this.outputStream = (PrintStream) newSpec.get("PrintStream").value;
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
}
