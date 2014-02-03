package gpigb.report;

import gpigb.data.*;

import java.util.List;

public class ReporterPlot implements Reporter {
	SimpleGrapher grapher = new SimpleGrapher();
	@Override
	public void GenerateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<Integer> data = (RecordSet<Integer>) dataStream.get(0);
		grapher.clear();
		grapher.plotData(data.getReadingAtPosition(0));
		}
	}