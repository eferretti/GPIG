package gpigb.testruns;

import gpigb.analyse.Analyser;
import gpigb.classloading.IDGenerator;
import gpigb.classloading.JarFileComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.handlers.GUIConfigHandler;
import gpigb.report.PortReporter;
import gpigb.report.Reporter;
import gpigb.report.TestAppGUI;
import gpigb.sense.PortSensor;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.Store;

import java.util.Map;

public class PortSensorHumsLocal {
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
//		Analyser aMean = new MeanAnalyser();
//		PortReporter rPort = new PortReporter();
		
		Integer aMeanID = aMgr.getModuleIDByName("gpigb.analyse.MeanAnalyser");
		Analyser aMean = (Analyser) aMgr.getObjectByID(aMgr.createObjectOfModule(aMeanID)).get();
		System.out.println("Mean Analyser : Component " + aMean.getID());
		
		Integer sPort1ID = seMgr.getModuleIDByName("gpigb.sense.PortSensor");
		Sensor<Double> s1 = (Sensor<Double>) seMgr.getObjectByID(seMgr.createObjectOfModule(sPort1ID)).get();
		System.out.println("Port Sensor : Component " + s1.getID());
		
		
		Integer sPort2ID = seMgr.getModuleIDByName("gpigb.sense.PortSensor");
		Sensor<Double> s2 = (Sensor<Double>) seMgr.getObjectByID(seMgr.createObjectOfModule(sPort2ID)).get();
		
		Integer stInMemID = stMgr.getModuleIDByName("gpigb.store.InMemoryStore");
		final Store st = stMgr.getObjectByID(stMgr.createObjectOfModule(stInMemID)).get();
		System.out.println("Store : Component " + st.getID());
		
		Integer rStateID = rMgr.getModuleIDByName("gpigb.report.TestAppGUI");
		Reporter rState = (Reporter) rMgr.getObjectByID(rMgr.createObjectOfModule(rStateID)).get();
		System.out.println("Gui Reporter : Component " + rState.getID());
		
		Integer rPortID = rMgr.getModuleIDByName("gpigb.report.PortReporter");
		Reporter rPort = (Reporter) rMgr.getObjectByID(rMgr.createObjectOfModule(rPortID)).get();
		System.out.println("Port Reporter : Component " + rPort.getID());
		
		Integer rtaThresholdID = aMgr.getModuleIDByName("gpigb.analyse.ThresholdAnalyser");
		Analyser rtaThreshold = (Analyser) aMgr.getObjectByID(aMgr.createObjectOfModule(rtaThresholdID)).get();
		System.out.println("Threshold Analyser : Component " + rtaThreshold.getID());
		
		GUIConfigHandler configHandler = new GUIConfigHandler(aMgr.getAvailableObjects(), rMgr.getAvailableObjects(), stMgr.getAvailableObjects(), seMgr.getAvailableObjects());
		
		Map<String, ConfigurationValue> config;
		
		config = aMean.getConfigSpec();
		configHandler.getConfiguration(config);
		aMean.setConfig(config, null, null, null, stMgr);
		
		
		config = rtaThreshold.getConfigSpec();
		configHandler.getConfiguration(config);
		rtaThreshold.setConfig(config, null, rMgr, null, stMgr);
		
		
		
		for(int i = 0; i < rState.getConfigurationStepNumber(); ++i)
		{
			config = rState.getConfigSpec();
			configHandler.getConfiguration(config);
			rState.setConfig(config, aMgr, rMgr, seMgr, null);
		}
		
		config = rPort.getConfigSpec();
		configHandler.getConfiguration(config);
		rPort.setConfig(config, aMgr, rMgr, seMgr, stMgr);
		
		config = s1.getConfigSpec();
		configHandler.getConfiguration(config);
		s1.setConfig(config, null, null, null, null);
		s1.registerObserver(st);
		s1.registerObserver((SensorObserver)rtaThreshold);
		
//		config = s2.getConfigSpec();
//		configHandler.getConfiguration(config);
//		s2.setConfig(config, null, null, null, null);
//		s2.registerObserver(st);
		
		while (true)
		{	
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e){}
			rState.generateReport(null);
		}
	}

}
