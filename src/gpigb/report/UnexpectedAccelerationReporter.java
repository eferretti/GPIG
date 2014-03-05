package gpigb.report;

import gpigb.configuration.ConfigurationHandler;
import gpigb.data.DataSet;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

/**
 * A reporter which is designed to be used with
 * {@link gpigb.analyse.AccelerationAnalyser}
 */
public class UnexpectedAccelerationReporter implements Reporter
{
	PrintStream outputStream;
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateReport(List<DataSet<?>> data)
	{
		DataSet<Integer> errorSigniture = (DataSet<Integer>) data.get(0);
		String errorMessage = "SensorID: " + errorSigniture.getSensorID() + " TimeFrom: "
				+ errorSigniture.getFromTime() + " TimeTo: " + errorSigniture.getToTime() + "\n";

		outputStream.print(errorMessage);
		
		Long timeFrame = errorSigniture.getToTime().getTime() - errorSigniture.getFromTime().getTime();

		Integer valueDifference = errorSigniture.getDataAtPosition(0).getData()
				- errorSigniture.getDataAtPosition(errorSigniture.getRecordCount() - 1).getData();

		errorMessage = "In " + timeFrame + "ms the sensor experianced a change of " + valueDifference + "\n";
		outputStream.println(errorMessage);
	}

	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, Object> config = new HashMap<>();
		config.put("PrintStream", null);
		
		handler.getConfiguration(config);
		
		outputStream = (PrintStream) (config.containsKey("PrintStream") ? config.get("PrintStream") : System.out);
	}
}
