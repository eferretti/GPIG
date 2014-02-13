package gpigb.report;

import gpigb.data.RecordSet;

import java.util.List;

/**
 * A reporter which is designed to be used with
 * {@link gpigb.analyse.AccelerationAnalyser}
 */
public class UnexpectedAccelerationReporter implements Reporter
{
	@Override
	public void generateReport(List<RecordSet<?>> data)
	{
		RecordSet<Integer> errorSigniture = (RecordSet<Integer>) data.get(0);
		String errorMessage = "SensorID: " + errorSigniture.getSensorID() + " TimeFrom: "
				+ errorSigniture.getFromTime() + " TimeTo: " + errorSigniture.getToTime() + "\n";

		System.out.print(errorMessage);
		Long timeFrame = errorSigniture.getToTime().getTime() - errorSigniture.getFromTime().getTime();

		Integer valueDifference = errorSigniture.getDataAtPosition(0).getData()
				- errorSigniture.getDataAtPosition(errorSigniture.getRecordCount() - 1).getData();

		errorMessage = "In " + timeFrame + "ms the sensor experianced a change of " + valueDifference + "\n";
	}
}
