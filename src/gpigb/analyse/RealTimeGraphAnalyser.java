package gpigb.analyse;

import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Plotter", new ConfigurationValue(ValueType.Reporter, 0));
		configSpec.put("Title", new ConfigurationValue(ValueType.String, ""));
		return configSpec;
	}
	
	@Override
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.plotter = rMgr.getObjectByID(newSpec.get("Plotter").intValue);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
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

	private int id;
	public void setID(int newID)
	{
		this.id = newID;
	}
	
	public int getID()
	{
		return this.id;
	}
}
