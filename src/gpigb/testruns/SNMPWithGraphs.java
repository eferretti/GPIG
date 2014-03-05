package gpigb.testruns;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gpigb.analyse.RealTimeGraphAnalyser;
import gpigb.analyse.ThresholdAnalyser;
import gpigb.report.Reporter;
import gpigb.report.ReporterPlotRTSmart;
import gpigb.report.SimpleWebReporter;
import gpigb.sense.SNMPSensor;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.InMemoryStore;
import gpigb.store.MongoStore;
import gpigb.store.Store;
import gpigb.analyse.Analyser;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;

public class SNMPWithGraphs
{
	public static void main(String[] args)
	{
		Sensor<Float> snmpSensor = new SNMPSensor();
		final Store inMemoryStore = new MongoStore();
		
		snmpSensor.registerObserver(new SensorObserver<Float>()
		{
			@Override
			public void update(DataRecord<Float> reading)
			{
				DataSet<Float> ds = new DataSet<>(reading.getTimestamp(), reading.getTimestamp(), reading.getSensorID());
				ds.addRecord(reading);
				inMemoryStore.write(ds);
			}
			
			@Override
			public void update(int sensorID, Float reading)
			{
				Calendar cal = Calendar.getInstance();
				Date time = cal.getTime();
				DataSet<Float> ds = new DataSet<>(time, time, sensorID);
				ds.addRecord(new DataRecord<Float>(sensorID, reading));
				inMemoryStore.write(ds);
			}
		});
		
		Analyser thresholdAnalyser = new ThresholdAnalyser();
		thresholdAnalyser.configure(new ConfigurationHandler()
		{
			@Override
			public void getConfiguration(Map<String, Object> configSpec)
			{
				configSpec.put("Min", new Integer(0));
				configSpec.put("Max", new Integer(32));
			}
		});
		
		Analyser realTimeGraphAnalyser = new RealTimeGraphAnalyser();
		final Reporter plotter = new ReporterPlotRTSmart();
		plotter.configure(new ConfigurationHandler()
		{
			@Override
			public void getConfiguration(Map<String, Object> configSpec)
			{
				configSpec.put("Title", "My Plotter");
				configSpec.put("Width", 400);
				configSpec.put("Height", 200);
			}
		});
		
		realTimeGraphAnalyser.configure(new ConfigurationHandler()
		{
			@Override
			public void getConfiguration(Map<String, Object> configSpec)
			{
				configSpec.put("Plotter", new StrongReference<Reporter>(plotter));
			}
		});
		
		Reporter webReporter = new SimpleWebReporter();
		webReporter.configure(new ConfigurationHandler()
		{
			
			@Override
			public void getConfiguration(Map<String, Object> configSpec)
			{
				configSpec.put("Store", new StrongReference<Store>(inMemoryStore));
			}
		});
		
		while(true);
	}
}
