package gpigb.report;

import gpigb.data.RecordSet;

import java.util.Date;
import java.util.List;

public class Reporter2 implements Reporter
{

	@Override
	public void GenerateReport(List<RecordSet<?>> data)
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
