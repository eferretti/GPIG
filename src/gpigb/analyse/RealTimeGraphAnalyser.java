package gpigb.analyse;

import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
import gpigb.report.Reporter;
import gpigb.sense.SensorObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RealTimeGraphAnalyser implements Analyser
{
	StrongReference<Reporter> plotter;

	ArrayList<DataSet<?>> al = new ArrayList<>();
	DataSet<Integer> rs;

	public RealTimeGraphAnalyser()
	{
	}

	public void update(int sensorID, Float reading)
	{
		al.clear();
		rs = new DataSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new DataRecord<Integer>(sensorID, reading.intValue()));
		al.add(rs);
		plotter.get().generateReport(al);
	}

	public void update(DataRecord<Float> reading)
	{
		al.clear();
		rs = new DataSet<>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 0);
		rs.addRecord(new DataRecord<Integer>(reading.getSensorID(), reading.getData().intValue()));
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
	public boolean analyse(List<DataSet<?>> input)
	{
		al.clear();
		al.add(input.get(0));
		plotter.get().generateReport(al);
		return true;
	}

	@Override
	public boolean analyse(DataSet<?> input)
	{
		al.clear();
		al.add(input);
		plotter.get().generateReport(al);
		return true;
	}
}
