package gpigb.report;

import gpigb.configuration.ConfigurationHandler;
import gpigb.data.DataSet;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

/**
 * Reports a Sensor that has a value out of range, it reports the Sensor, time
 * and value.
 */
public class OutOfRangeReporter implements Reporter
{
	PrintStream outputStream;
	
	public void generateReport(List<DataSet<?>> EroniousData)
	{
		if (EroniousData.isEmpty()) return;

		String ErrorMessage = "SensorID: " + EroniousData.get(0).getSensorID() + " Time: "
				+ EroniousData.get(0).getDataAtPosition(0).getTimestamp() + " Reading: "
				+ EroniousData.get(0).getDataAtPosition(0).getData() + "\n";
		outputStream.print(ErrorMessage);
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
