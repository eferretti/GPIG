package gpigb.report;

import gpigb.data.*;

import java.util.List;

public class ReporterPlot implements Reporter {
	String title;
	SimpleGrapher grapher;
	
	public ReporterPlot(String title){
		this.title = title;
		grapher = new SimpleGrapher(title, 800, 500);
	}
	
	@Override
	public void generateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<Integer> data = (RecordSet<Integer>) dataStream.get(0);
		grapher.clear();
		grapher.plotData(data.getReadingAtPosition(0));
		}
	}