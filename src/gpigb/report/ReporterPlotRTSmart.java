package gpigb.report;

import gpigb.data.*;

import java.util.List;

public class ReporterPlotRTSmart implements Reporter {
	String title;
	SmartGrapher grapher;
	
	public ReporterPlotRTSmart(String title){
		this.title = title;
		grapher = new SmartGrapher(title, 800, 500);
	}
	
	@Override
	public void generateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<?> dataSet = dataStream.get(0);
		int data = (Integer)dataSet.getReadingAtPosition(0).getData();
		grapher.plot(data);
		}
	}


