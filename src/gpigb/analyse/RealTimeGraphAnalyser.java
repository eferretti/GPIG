package gpigb.analyse;

import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.sense.SensorObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RealTimeGraphAnalyser implements Analyser
{
	StrongReference<Reporter> plotter;

	ArrayList<RecordSet<?>> al = new ArrayList<>();
	RecordSet<Integer> rs;

	public RealTimeGraphAnalyser()
	{
	}

	public void update(int sensorID, Float reading)
	{
		al.clear();
		rs = new RecordSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new SensorRecord<Integer>(sensorID, reading.intValue()));
		al.add(rs);
		plotter.get().generateReport(al);
	}

	public void update(SensorRecord<Float> reading)
	{
		al.clear();
		rs = new RecordSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new SensorRecord<Integer>(reading.getSensorID(), reading.getData().intValue()));
		al.add(rs);
		plotter.get().generateReport(al);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Plotter", new ConfigurationValue(ValueType.Reporter, null));
		configSpec.put("Title", new ConfigurationValue(ValueType.String, null));
		handler.getConfiguration(configSpec);
		this.plotter = (StrongReference<Reporter>) configSpec.get("Plotter").value;
		System.out.println("New title: " + configSpec.get("Title").value);
	}

	@Override
	public boolean analyse(List<RecordSet<?>> input)
	{
		al.clear();
		al.add(input.get(0));
		plotter.get().generateReport(al);
		return true;
	}

	@Override
	public boolean analyse(RecordSet<?> input)
	{
		al.clear();
		al.add(input);
		plotter.get().generateReport(al);
		return true;
	}
}
