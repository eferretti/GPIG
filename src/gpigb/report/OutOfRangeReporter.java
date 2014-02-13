package gpigb.report;

import gpigb.data.DataSet;

import java.util.List;

/**
 * Reports a Sensor that has a value out of range, it reports the Sensor, time
 * and value.
 */
public class OutOfRangeReporter implements Reporter
{

	public void generateReport(List<DataSet<?>> EroniousData)
	{
		if (EroniousData.isEmpty()) return;

		String ErrorMessage = "SensorID: " + EroniousData.get(0).getSensorID() + " Time: "
				+ EroniousData.get(0).getDataAtPosition(0).getTimestamp() + " Reading: "
				+ EroniousData.get(0).getDataAtPosition(0).getData() + "\n";
		System.out.print(ErrorMessage);
	}

}
