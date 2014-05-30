package gpigb.testruns;

import gpigb.analyse.Analyser;
import gpigb.analyse.NullAnalyser;
import gpigb.analyse.RTNullAnalyser;
import gpigb.analyse.RealTimeAnalyser;
import gpigb.analyse.RealTimeGraphAnalyser;
import gpigb.classloading.IDGenerator;
import gpigb.classloading.JarFileComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.handlers.GUIConfigHandler;
import gpigb.data.RecordSet;
import gpigb.report.Reporter;
import gpigb.report.TestAppGUI;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class EmailerTest {
	public static void main(String[] args) throws InterruptedException
	{
		IDGenerator.setMinID(47);
		
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
			
//		Sensor<Double> s1 = new PortSensor();
//		final InMemoryStore st = new InMemoryStore();

//		TestAppGUI rState = new TestAppGUI();
		
		Integer aRTNullID = aMgr.getModuleIDByName("gpigb.analyse.RTNullAnalyser");		
		RealTimeAnalyser aRTNull = (RealTimeAnalyser) aMgr.getObjectByID(aMgr.createObjectOfModule(aRTNullID)).get();
		
		Integer sPort1ID = seMgr.getModuleIDByName("gpigb.sense.RandomValueSensor");
		Sensor<Integer> s1 = (Sensor<Integer>) seMgr.getObjectByID(seMgr.createObjectOfModule(sPort1ID)).get();
		
		Integer stInMemID = stMgr.getModuleIDByName("gpigb.store.InMemoryStore");
		final Store st = stMgr.getObjectByID(stMgr.createObjectOfModule(stInMemID)).get();
		
		Integer rStateID = rMgr.getModuleIDByName("gpigb.report.EmailReporter");
		Reporter rState = (Reporter) rMgr.getObjectByID(rMgr.createObjectOfModule(rStateID)).get();
		
		GUIConfigHandler configHandler = new GUIConfigHandler(aMgr.getAvailableObjects(), rMgr.getAvailableObjects(), stMgr.getAvailableObjects(), seMgr.getAvailableObjects());
		
		Map<String, ConfigurationValue> config;		
		config = s1.getConfigSpec();
		configHandler.getConfiguration(config);
		s1.setConfig(config, null, null, null, null);
			
		config = rState.getConfigSpec();
		configHandler.getConfiguration(config);
		rState.setConfig(config, aMgr, null, seMgr, null);
		
		config = aRTNull.getConfigSpec();
		configHandler.getConfiguration(config);
		aRTNull.setConfig(config, null, rMgr, null, stMgr);
		
		s1.registerObserver(st);
		s1.registerObserver(aRTNull);
		/*
		while (true)
		{	
			try
			{
				Calendar before = Calendar.getInstance();
				Thread.sleep(10000);
				RecordSet<Double> saveRecord = new RecordSet(before.getTime(), Calendar.getInstance().getTime(), s1.getID());
				aRTNull.analyse(saveRecord);
				ArrayList<RecordSet<?>> newList = new ArrayList<>();
				newList.add(saveRecord);
				rState.generateReport(newList);
			}
			catch(Exception e){}
			
		}
		*/
		
	}
}
