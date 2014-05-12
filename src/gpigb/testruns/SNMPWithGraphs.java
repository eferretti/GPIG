package gpigb.testruns;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import gpigb.analyse.RealTimeGraphAnalyser;
//import gpigb.analyse.ThresholdAnalyser;
import gpigb.report.Reporter;
import gpigb.report.ReporterPlotRTSmart;
import gpigb.sense.SNMPSensor;
//import gpigb.report.ReporterPlotRTSmart;
//import gpigb.report.SimpleWebReporter;
//import gpigb.sense.SNMPSensor;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.MongoStore;
//import gpigb.store.InMemoryStore;
//import gpigb.store.MongoStore;
import gpigb.store.Store;
import gpigb.analyse.Analyser;
import gpigb.analyse.RealTimeGraphAnalyser;
import gpigb.analyse.ThresholdAnalyser;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.configuration.handlers.GUIConfigHandler;
import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;

public class SNMPWithGraphs
{
	public static void main(String[] args) throws InterruptedException
	{
		JarFileComponentManager<Analyser> aMgr = new JarFileComponentManager<>(Analyser.class);
		JarFileComponentManager<Reporter> rMgr = new JarFileComponentManager<>(Reporter.class);
		JarFileComponentManager<Sensor> seMgr = new JarFileComponentManager<>(Sensor.class);
		JarFileComponentManager<Store> stMgr = new JarFileComponentManager<>(Store.class);
		
		aMgr.addModuleDirectory("~/HUMS_Modules");
		rMgr.addModuleDirectory("~/HUMS_Modules");
		seMgr.addModuleDirectory("~/HUMS_Modules");
		stMgr.addModuleDirectory("~/HUMS_Modules");
		
		aMgr.refreshModules();
		rMgr.refreshModules();
		seMgr.refreshModules();
		stMgr.refreshModules();
		
		Sensor<Float> snmpSensor = new SNMPSensor();
		final Store inMemoryStore = new MongoStore();
		Analyser thresholdAnalyser = new ThresholdAnalyser();
		Analyser realTimeGraphAnalyser = new RealTimeGraphAnalyser();
		
		ModuleSummary s = rMgr.getAvailableModules().get(0);
		rMgr.createObjectOfModule(s.moduleID);
		
//		snmpSensor.registerObserver(new SensorObserver<Float>()
//		{
//			@Override
//			public void update(SensorRecord<Float> reading)
//			{
//				RecordSet<Float> ds = new RecordSet<>(reading.getTimestamp(), reading.getTimestamp(), reading.getSensorID());
//				ds.addRecord(reading);
//				inMemoryStore.write(ds);
//			}
//			
//			@Override
//			public void update(int sensorID, Float reading)
//			{
//				Calendar cal = Calendar.getInstance();
//				Date time = cal.getTime();
//				RecordSet<Float> ds = new RecordSet<>(time, time, sensorID);
//				ds.addRecord(new SensorRecord<Float>(sensorID, reading));
//				inMemoryStore.write(ds);
//			}
//		});
		
		thresholdAnalyser.configure(new GUIConfigHandler(aMgr, rMgr, stMgr, seMgr)); 
//		ConfigurationHandler()
//		{
//			@Override
//			public void getConfiguration(Map<String, ConfigurationValue> configSpec)
//			{
//				configSpec.put("Min", new ConfigurationValue(ValueType.Integer, new Integer(0)));
//				configSpec.put("Max", new ConfigurationValue(ValueType.Integer, new Integer(32)));
//			}
//		});
		
		realTimeGraphAnalyser.configure(new GUIConfigHandler(aMgr, rMgr, stMgr, seMgr));
		
//		Reporter webReporter = new SimpleWebReporter();
//		webReporter.configure(new ConfigurationHandler()
//		{
//			
//			@Override
//			public void getConfiguration(Map<String, ConfigurationValue> configSpec)
//			{
//				configSpec.put("Store", new ConfigurationValue(ValueType.Store, new StrongReference<Store>(inMemoryStore)));
//			}
//		});
		
		while(true)
		{
			Thread.sleep(10);
		}
	}
}
