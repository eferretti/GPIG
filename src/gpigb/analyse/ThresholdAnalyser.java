package gpigb.analyse;

import gpigb.configuration.ConfigurationHandler;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
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

	@SuppressWarnings("unchecked")
	public boolean analyse(DataSet<?> input)
	{
		DataSet<Integer> inputs = (DataSet<Integer>) input;
		int size = inputs.getRecordCount();
		boolean r = false;

		for (int i = 0; i < size; i++) {
			DataRecord<Integer> rec = inputs.getDataAtPosition(i);
			if (rec.getData() < lowerThreshold || rec.getData() > upperThreshold) {

				r = true;

				List<DataSet<?>> rt = new ArrayList<DataSet<?>>();
				DataSet<Integer> add = new DataSet<Integer>(inputs.getFromTime(), inputs.getToTime(),
						inputs.getSensorID());
				add.addRecord(inputs.getDataAtPosition(i));
				rt.add(add);

				new OutOfRangeReporter().generateReport(rt);

			}

		}

		return r;

	}

	public boolean analyse(List<DataSet<?>> input)
	{
		return false;
	}

	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, Object> configSpec = new HashMap<>();
		configSpec.put("Min", Integer.MIN_VALUE);
		configSpec.put("Max", Integer.MAX_VALUE);
		
		handler.getConfiguration(configSpec);
		
		this.lowerThreshold = ((Integer)configSpec.get("Min")).intValue();
		this.upperThreshold = ((Integer)configSpec.get("Max")).intValue();
	}

}
