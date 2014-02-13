package gpigb.analyse;

import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;
import gpigb.report.ReporterPlotRTSmart;
import gpigb.sense.SensorObserver;

import java.util.ArrayList;
import java.util.Calendar;

public class RealTimeGraphAnalyser implements SensorObserver<Float>
{
	ReporterPlotRTSmart plotter = new ReporterPlotRTSmart("");

	ArrayList<RecordSet<?>> al = new ArrayList<>();
	RecordSet<Integer> rs;

	public RealTimeGraphAnalyser()
	{
	}

	@Override
	public void update(int sensorID, Float reading)
	{
		al.clear();
		rs = new RecordSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new SensorRecord<Integer>(sensorID, reading.intValue()));
		al.add(rs);
		plotter.generateReport(al);
	}

	@Override
	public void update(SensorRecord<Float> reading)
	{
		al.clear();
		rs = new RecordSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new SensorRecord<Integer>(reading.getSensorID(), reading.getData().intValue()));
		al.add(rs);
		plotter.generateReport(al);
	}
}
