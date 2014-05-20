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

import com.sun.mail.dsn.Report;

public class RTNullAnalyser implements RealTimeAnalyser {
	private int id;
	private StrongReference<Reporter> reporter;
	@Override
	public boolean analyse(List<RecordSet<?>> input) {
		
		return false;
	}

	@Override
	public boolean analyse(RecordSet<?> input) {
		
		return false;
	}

	@Override
	public void setID(int newID) {
		// TODO Auto-generated method stub
		this.id = newID;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public int getConfigurationStepNumber() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec() {
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Reporter", new ConfigurationValue(ValueType.Reporter, 0));
		return configMap;
	}

	@Override
	public boolean setConfig(Map<String, ConfigurationValue> newConfig,
			ComponentManager<Analyser> aMgr,ComponentManager<Reporter> rMgr,
			ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr) {
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
	public boolean update(int sensorID, Integer reading) {
		RecordSet<Integer> rs = new RecordSet<Integer>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), sensorID);
		rs.addRecord(new SensorRecord<Integer>(sensorID, reading));
		List<RecordSet<?>> ls = new ArrayList();
		ls.add(rs);
		reporter.get().generateReport(ls);
		return true;
	}

	@Override
	public boolean update(int sensorID, Double reading) {
		return this.update(sensorID, (int) Math.round(reading));
//		RecordSet<Double> rs = new RecordSet<Double>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), sensorID);
//		rs.addRecord(new SensorRecord<Double>(sensorID, reading));
//		List<RecordSet<?>> ls = new ArrayList();
//		ls.add(rs);
//		reporter.get().generateReport(ls);
//		return true;
	}

	@Override
	public boolean update(int sensorID, String reading) {
		RecordSet<String> rs = new RecordSet<String>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), sensorID);
		rs.addRecord(new SensorRecord<String>(sensorID, reading));
		List<RecordSet<?>> ls = new ArrayList();
		ls.add(rs);
		reporter.get().generateReport(ls);
		return true;
	}

	@Override
	public boolean update(SensorRecord<?> reading) {
		//RecordSet<?> rs = new RecordSet<String>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), reading.getSensorID());
		//rs.addRecord(reading);
		//List<RecordSet<?>> ls = new ArrayList();
		//ls.add(rs);
		//reporter.get().generateReport(ls);
		return false;
	}

}
