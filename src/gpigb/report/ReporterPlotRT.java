package gpigb.report;

import gpigb.data.RecordSet;

import java.util.List;

public class ReporterPlotRT implements Reporter {
	String title;
	SimpleGrapher grapher;
	
	public ReporterPlotRT(String title){
		this.title = title;
		grapher = new SimpleGrapher(title, 800, 500);
	}
	
	@Override
	public void generateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<?> data = dataStream.get(0);
		grapher.plotData(data.getReadingAtPosition(0));
		}
	}


