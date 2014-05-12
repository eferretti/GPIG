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
import gpigb.report.OutOfRangeReporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

<<<<<<< HEAD
	public boolean analyse(RecordSet<?> input)
=======
	@SuppressWarnings("unchecked")
	public boolean analyse(DataSet<?> input)
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Min", new ConfigurationValue(ValueType.Integer, Integer.MIN_VALUE));
		configSpec.put("Max", new ConfigurationValue(ValueType.Integer, Integer.MAX_VALUE));
		
		handler.getConfiguration(configSpec);
		
		this.lowerThreshold = ((Integer)configSpec.get("Min").value).intValue();
		this.upperThreshold = ((Integer)configSpec.get("Max").value).intValue();
	}

}
