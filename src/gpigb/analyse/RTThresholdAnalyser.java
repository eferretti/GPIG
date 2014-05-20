package gpigb.analyse;

import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.OutOfRangeReporter;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RTThresholdAnalyser implements RealTimeAnalyser
{
	private int id;
	private int upperThreshold;
	private int lowerThreshold;
	private StrongReference<Reporter> reporter;

	@Override
	public boolean analyse(List<RecordSet<?>> input)
	{
		return false;
	}

	@Override
	public boolean analyse(RecordSet<?> input)
	{
		RecordSet<Integer> inputs = (RecordSet<Integer>) input;
		int size = inputs.getRecordCount();
		boolean r = false;

		for (int i = 0; i < size; i++) {
			SensorRecord<Integer> rec = inputs.getDataAtPosition(i);
			if (rec.getData() < lowerThreshold || rec.getData() > upperThreshold) {

				r = true;

				List<RecordSet<?>> rt = new ArrayList<RecordSet<?>>();
				RecordSet<Integer> add = new RecordSet<Integer>(inputs.getFromTime(), inputs.getToTime(),
						inputs.getSensorID());
				add.addRecord(inputs.getDataAtPosition(i));
				rt.add(add);

				new OutOfRangeReporter().generateReport(rt);

			}

		}

		return r;
	}

	@Override
	public void setID(int newID)
	{
		this.id = newID;
		
	}

	@Override
	public int getID()
	{
		return this.id;
	}

	@Override
	public int getConfigurationStepNumber()
	{
		return 1;
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Reporter", new ConfigurationValue(ValueType.Reporter, 0));
		return configMap;
	}

	@Override
	public boolean setConfig(Map<String, ConfigurationValue> newConfig,
			ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr,
			ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.reporter = rMgr.getObjectByID(newConfig.get("Reporter").intValue);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(int sensorID, Integer reading)
	{
		RecordSet<Integer> rs = new RecordSet<Integer>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), sensorID);
		rs.addRecord(new SensorRecord<Integer>(sensorID, reading));
		List<RecordSet<?>> ls = new ArrayList();
		ls.add(rs);
		reporter.get().generateReport(ls);
		return true;
	}

	@Override
	public boolean update(int sensorID, Double reading)
	{
		return this.update(sensorID, (int) Math.round(reading));
	}

	@Override
	public boolean update(int sensorID, String reading)
	{
		RecordSet<String> rs = new RecordSet<String>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), sensorID);
		rs.addRecord(new SensorRecord<String>(sensorID, reading));
		List<RecordSet<?>> ls = new ArrayList();
		ls.add(rs);
		reporter.get().generateReport(ls);
		return true;
	}

	@Override
	public boolean update(SensorRecord<?> reading)
	{
		return false;
	}

}
