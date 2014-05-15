package gpigb.analyse;

import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;
import gpigb.analyse.Analyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An analyser which reports significant acceleration in a sensor reading
 */
public class AccelerationAnalyser implements Analyser
{
	Integer threshold = Integer.MAX_VALUE;
	
	/**
	 * Perform analysis on sensor reading histories. If any history has a value
	 * which changes by more than a given threshold then report only the
	 * offending histories.
	 * 
	 * @param data
	 *            The set of sensor histories to analyse
	 */
	public synchronized boolean analyse(List<RecordSet<?>> data)
	{
		List<RecordSet<?>> changeRecord = new ArrayList<RecordSet<?>>();
		List<RecordSet<?>> averageRecord = new ArrayList<RecordSet<?>>();

		Integer previousReading = 0;
		for (int j = 0; j < data.size(); j++) {
			// No data has been provided
			if (data.get(j).getRecordCount() == 0) return false;

			Integer average = (Integer) data.get(j).getDataAtPosition(0).getData();
			previousReading = average;
			for (int i = 1; i < data.get(j).getRecordCount(); i++) {
				Integer currentReading = (Integer) data.get(j).getDataAtPosition(i).getData();
				average = average + currentReading;

				// If there is a difference between readings add the RecordSet
				// where the change occurred
				if (currentReading > previousReading + threshold || currentReading < previousReading - threshold)
					changeRecord.add(data.get(j));

				previousReading = (Integer) data.get(j).getDataAtPosition(i).getData();
			}

			// Calculate average
			average = average / data.get(j).getRecordCount();

			// Create new record and add to list
			RecordSet<Integer> av = new RecordSet<Integer>(data.get(j).getFromTime(), data.get(j).getToTime(), data.get(j)
					.getSensorID());
			SensorRecord<Integer> as = new SensorRecord<Integer>(data.get(j).getSensorID(), average);
			av.addRecord(as);
			averageRecord.add(av);
		}

		return true;
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Threshold", new ConfigurationValue(ValueType.Integer, threshold));
		return configSpec;
	}
	
	@Override
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.threshold = newSpec.get("Threshold").intValue;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean analyse(RecordSet<?> input)
	{
		// TODO Auto-generated method stub
		return false;
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
