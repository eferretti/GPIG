package gpigb.report;

<<<<<<< HEAD
import gpigb.data.RecordSet;
=======
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.DataSet;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

/**
 * Reports a Sensor that has a value out of range, it reports the Sensor, time
 * and value.
 */
public class OutOfRangeReporter implements Reporter
{
<<<<<<< HEAD

	public void generateReport(List<RecordSet<?>> EroniousData)
=======
	PrintStream outputStream;
	
	public void generateReport(List<DataSet<?>> EroniousData)
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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
		HashMap<String, ConfigurationValue> config = new HashMap<>();
		config.put("PrintStream", new ConfigurationValue(ValueType.OutStream, null));
		
		handler.getConfiguration(config);
		
		outputStream = (PrintStream) (config.get("PrintStream") != null ? config.get("PrintStream").value : System.out);
	}

}
