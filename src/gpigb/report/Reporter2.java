package gpigb.report;

import gpigb.data.RecordSet;

import java.util.List;

/**
 * A reporter which is designed to be used with {@link gpigb.analyse.Analyser2}
 * @author rar500
 *
 */
public class Reporter2 implements Reporter
{

	@Override
	public void generateReport(List<RecordSet<?>> data)
	{
		RecordSet<Integer> errorSigniture = (RecordSet<Integer>) data.get(0);
		String errorMessage = "SensorID: " + errorSigniture.getSensorID()
				+ " TimeFrom: " + errorSigniture.getFromTime() + " TimeTo: "
				+ errorSigniture.getToTime() + "\n";
		
		System.out.print(errorMessage);
		Long timeFrame = errorSigniture.getToTime().getTime()
				- errorSigniture.getFromTime().getTime();
		
		Integer valueDifference = errorSigniture.getReadingAtPosition(0)
				.getData()
				- errorSigniture.getReadingAtPosition(
						errorSigniture.getRecordCount() - 1).getData();
		
		errorMessage = "In " + timeFrame
				+ "ms the sensor experianced a change of " + valueDifference
				+ "\n";
	}
}
