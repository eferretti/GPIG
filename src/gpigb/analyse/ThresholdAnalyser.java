package gpigb.analyse;

import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;
import gpigb.report.OutOfRangeReporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An analyser which takes an upper and lower bound and reports when sensor
 * readings exceed these values
 */
public class ThresholdAnalyser implements Analyser
{
	int upperThreshold;
	int lowerThreshold;

	public ThresholdAnalyser()
	{
	}

	@SuppressWarnings("unchecked")
	public boolean analyse(RecordSet<?> input)
	{
		RecordSet<Integer> inputs = (RecordSet<Integer>) input;
		int size = inputs.getRecordCount();
		boolean r = false;

		for (int i = 0; i < size; i++) {
			SensorRecord<Integer> rec = inputs.getDataAtPosition(i);
			if (rec.getData() < lowerThreshold || rec.getData() > upperThreshold) {

				r = true;

				List<RecordSet<?>> rt = new ArrayList<RecordSet<?>>();
				RecordSet<Integer> add = new RecordSet<Integer>(inputs.getFromTime(), inputs.getToTime(),
						inputs.getSensorID());
				add.addRecord(inputs.getDataAtPosition(i));
				rt.add(add);

				new OutOfRangeReporter().generateReport(rt);

			}

		}

		return r;

	}

	public boolean analyse(List<RecordSet<?>> input)
	{
		return false;
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Min", new ConfigurationValue(ValueType.Integer, Integer.MIN_VALUE));
		configSpec.put("Max", new ConfigurationValue(ValueType.Integer, Integer.MAX_VALUE));
		return configSpec;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec)
	{
		try
		{
			this.lowerThreshold = (Integer) newSpec.get("Min").value;
			this.upperThreshold = (Integer) newSpec.get("Max").value;
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
