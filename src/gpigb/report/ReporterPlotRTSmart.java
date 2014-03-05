package gpigb.report;

import gpigb.configuration.ConfigurationHandler;
import gpigb.data.DataSet;

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
	public void generateReport(List<DataSet<?>> dataStream)
	{
		DataSet<?> dataSet = dataStream.get(0);
		int data = (Integer) dataSet.getDataAtPosition(0).getData();
		grapher.plot(data);
	}

	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, Object> configMap = new HashMap<>();
		configMap.put("Title", "Real Time Graph");
		configMap.put("Width", new Integer(900));
		configMap.put("Height", new Integer(500));
		
		handler.getConfiguration(configMap);
		
		this.title = (String) configMap.get("Title");
		grapher = new SmartGrapher(title, ((Integer)configMap.get("Width")).intValue(), ((Integer)configMap.get("Height")).intValue());
	}
}
