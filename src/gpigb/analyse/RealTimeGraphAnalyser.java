package gpigb.analyse;

import gpigb.data.DataRecord;
import gpigb.data.DataSet;
import gpigb.report.ReporterPlotRTSmart;
import gpigb.sense.SensorObserver;

import java.util.ArrayList;
import java.util.Calendar;

public class RealTimeGraphAnalyser implements SensorObserver<Float>
{
	ReporterPlotRTSmart plotter = new ReporterPlotRTSmart("");

	ArrayList<DataSet<?>> al = new ArrayList<>();
	DataSet<Integer> rs;

	public RealTimeGraphAnalyser()
	{
	}

	@Override
	public void update(int sensorID, Float reading)
	{
		al.clear();
		rs = new DataSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new DataRecord<Integer>(sensorID, reading.intValue()));
		al.add(rs);
		plotter.generateReport(al);
	}

	@Override
	public void update(DataRecord<Float> reading)
	{
		al.clear();
		rs = new DataSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new DataRecord<Integer>(reading.getSensorID(), reading.getData().intValue()));
		al.add(rs);
		plotter.generateReport(al);
	}
}
