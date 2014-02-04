package gpigb.report;

import gpigb.data.*;

import java.util.List;

public class ReporterPlotRTSmart implements Reporter {
	String title;
	SmartGrapher grapher;
	
	public ReporterPlotRTSmart(String title){
		this.title = title;
		grapher = new SmartGrapher(800, 500, 300);
	}
	
	@Override
	public void GenerateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<?> data = dataStream.get(0);
		grapher.plot((Integer)data.getReadingAtPosition(0).getData());
		}
	}


