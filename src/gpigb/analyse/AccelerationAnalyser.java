package gpigb.analyse;

<<<<<<< HEAD
import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;
=======
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
<<<<<<< HEAD
	public boolean analyse(List<RecordSet<?>> data)
=======
	public synchronized boolean analyse(List<DataSet<?>> data)
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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

	/**
	 * Invokes {@link gpigb.analyse.AccelerationAnalyser#analyse(List)} with a
	 * list containing the argument
	 * 
	 * @see gpigb.analyse.Analyser#analyse(RecordSet)
	 */
	public boolean analyse(RecordSet<?> input)
	{
		ArrayList<RecordSet<?>> a = new ArrayList<>();
		a.add(input);
		return analyse(a);
	}

	@Override
	public synchronized void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Threshold", new ConfigurationValue(ValueType.Integer, threshold));
		handler.getConfiguration(configSpec);
		this.threshold = (Integer) configSpec.get("Threshold").value;
	}

}
