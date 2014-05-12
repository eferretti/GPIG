package gpigb.report;

<<<<<<< HEAD
import gpigb.data.RecordSet;
=======
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.DataSet;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89

import java.util.HashMap;
import java.util.List;

import com.mongodb.util.Hash;

/**
 * A Real Time graph which can be used to plot sensor data
 */
public class ReporterPlotRTSmart implements Reporter
{
	String title;
	SmartGrapher grapher;

	public ReporterPlotRTSmart()
	{
	}

	@Override
	public void generateReport(List<RecordSet<?>> dataStream)
	{
		RecordSet<?> dataSet = dataStream.get(0);
		int data = (Integer) dataSet.getDataAtPosition(0).getData();
		grapher.plot(data);
	}

	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Title", new ConfigurationValue(ValueType.String, "Real Time Graph"));
		configMap.put("Width", new ConfigurationValue(ValueType.Integer, new Integer(900)));
		configMap.put("Height", new ConfigurationValue(ValueType.Integer, new Integer(500)));
		
		handler.getConfiguration(configMap);
		
		this.title = (String) configMap.get("Title").value;
		grapher = new SmartGrapher(title, ((Integer)configMap.get("Width").value).intValue(), ((Integer)configMap.get("Height").value).intValue());
	}
	
	public String toString()
	{
		return "Smart Plotter: " + title;
	}
}
