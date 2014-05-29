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

/**
 * An analyser which takes an upper and lower bound and reports when sensor
 * readings exceed these values
 */
public class ThresholdAnalyser implements RealTimeAnalyser
{
	private int upperThreshold;
	private int lowerThreshold;
	private StrongReference<Reporter> reporter;
	
	public ThresholdAnalyser()
	{
	}

	@SuppressWarnings("unchecked")
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

			}

		}

		return r;

	}

	public boolean analyse(List<RecordSet<?>> input)
	{
		return false;
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Min", new ConfigurationValue(ValueType.Integer, lowerThreshold));
		configSpec.put("Max", new ConfigurationValue(ValueType.Integer, upperThreshold));
		configSpec.put("Reporter", new ConfigurationValue(ValueType.Reporter, 0));
		configSpec.put("Sensor", new ConfigurationValue(ValueType.Sensor, 0));
		return configSpec;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.lowerThreshold = (Integer) newSpec.get("Min").intValue;
			this.upperThreshold = (Integer) newSpec.get("Max").intValue;
			this.reporter = rMgr.getObjectByID(newSpec.get("Reporter").intValue);
			seMgr.getObjectByID(newSpec.get("Sensor").intValue).get().registerObserver(this);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
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
	
	@Override
	public int getConfigurationStepNumber() {
		
		return 1;
	}

	@Override
	public boolean update(int sensorID, Integer reading) {
		if (reading < lowerThreshold || reading > upperThreshold) {

			List<RecordSet<?>> rt = new ArrayList<RecordSet<?>>();
			RecordSet<Integer> add = new RecordSet<Integer>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
					sensorID);
			add.addRecord(new SensorRecord<Integer>(sensorID, 1 ));
			rt.add(add);

			reporter.get().generateReport(rt);
			return true;
		}
		return true;
	}

	@Override
	public boolean update(int sensorID, Double reading) {
		if (reading < lowerThreshold || reading > upperThreshold) {
			System.out.println("Threshold Analyser , Sending a signal");
			List<RecordSet<?>> rt = new ArrayList<RecordSet<?>>();
			RecordSet<Integer> add = new RecordSet<Integer>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
					sensorID);
			add.addRecord(new SensorRecord<Integer>(sensorID, 1 ));
			rt.add(add);

			reporter.get().generateReport(rt);
			return true;
		}
		System.out.println("Threshold Analyser , Not Sending Signal");
		return true;
	}

	@Override
	public boolean update(int sensorID, String reading) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(SensorRecord<?> reading) {
		if ((Integer)reading.getData() < lowerThreshold || (Integer)reading.getData() > upperThreshold) {
			System.out.println("Threshold Analyser , Sending a signal");
			List<RecordSet<?>> rt = new ArrayList<RecordSet<?>>();
			RecordSet<Integer> add = new RecordSet<Integer>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
					reading.getSensorID());
			add.addRecord(new SensorRecord<Integer>(reading.getSensorID(), 1 ));
			rt.add(add);

			reporter.get().generateReport(rt);
			return true;
		}
		System.out.println("Threshold Analyser , Not Sending Signal");
		return true;
	}
}
