package gpigb.report;

import gpigb.data.*;

import java.util.List;

public class ReporterPlotRT implements Reporter {
	SimpleGrapher grapher = new SimpleGrapher();
	@Override
	public void GenerateReport(List<RecordSet<?>> dataStream) {	 
		RecordSet<Integer> data = (RecordSet<Integer>) dataStream.get(0);
		grapher.plotData(data.getReadingAtPosition(0));
		}
	}


