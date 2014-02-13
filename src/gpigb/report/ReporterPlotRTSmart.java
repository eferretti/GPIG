package gpigb.report;

import gpigb.data.DataSet;

import java.util.List;

/**
 * A Real Time graph which can be used to plot sensor data
 */
public class ReporterPlotRTSmart implements Reporter
{
	String title;
	SmartGrapher grapher;

	public ReporterPlotRTSmart(String title)
	{
		this.title = title;
		grapher = new SmartGrapher(title, 900, 500);
	}

	@Override
	public void generateReport(List<DataSet<?>> dataStream)
	{
		DataSet<?> dataSet = dataStream.get(0);
		int data = (Integer) dataSet.getDataAtPosition(0).getData();
		grapher.plot(data);
	}
}
