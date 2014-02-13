package gpigb.analyse;

import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;
import gpigb.report.OutOfRangeReporter;

import java.util.ArrayList;
import java.util.List;

/**
 * An analyser which takes an upper and lower bound and reports when sensor
 * readings exceed these values
 */
public class ThresholdAnalyser implements Analyser
{

	int upperThreshold;
	int lowerThreshold;

	public ThresholdAnalyser(int u, int l)
	{

		upperThreshold = u;
		lowerThreshold = l;

	}

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

}
