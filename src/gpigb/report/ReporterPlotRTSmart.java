package gpigb.report;

import gpigb.data.*;

import java.util.List;

public class ReporterPlotRTSmart implements Reporter {
	String title;
	SmartGrapher grapher;
	
	public ReporterPlotRTSmart(String title){
		this.title = title;
		grapher = new SmartGrapher(800, 500);
	}
	
	@Override
	public void generateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<?> data = dataStream.get(0);
		grapher.plot(data.getReadingAtPosition(0));
		}
	}


