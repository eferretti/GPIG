package gpigb.report;

import gpigb.data.RecordSet;

import java.util.List;

/**
 * Reports a Sensor that has a value out of range, it reports the Sensor, time
 * and value.
 */
public class Reporter1 implements Reporter
{

	public void generateReport(List<RecordSet<?>> EroniousData)
	{
		if(EroniousData.isEmpty())
			return;
		
		String ErrorMessage = "SensorID: " + EroniousData.get(0).getSensorID()
				+ " Time: " + EroniousData.get(0).getReadingAtPosition(0).getTimestamp() + " Reading: "
				+ EroniousData.get(0).getReadingAtPosition(0).getData() + "\n";
		System.out.print(ErrorMessage);
	}

}
