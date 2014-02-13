package gpigb.report;

import gpigb.data.RecordSet;

import java.util.List;

/**
 * A Real Time graoh which can be used to plot sensor data
 */
public class ReporterPlotRTSmart implements Reporter {
	String title;
	SmartGrapher grapher;
	
	public ReporterPlotRTSmart(String title){
		this.title = title;
		grapher = new SmartGrapher(title, 900, 500);
	}
	
	@Override
	public void generateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<?> dataSet = dataStream.get(0);
		int data = (Integer)dataSet.getReadingAtPosition(0).getData();
		grapher.plot(data);
		}
	}


