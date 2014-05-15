package gpigb.testruns;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gpigb.analyse.Analyser;
import gpigb.analyse.MeanAnalyser;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.classloading.IDGenerator;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.handlers.GUIConfigHandler;
import gpigb.external.HUMSSystem;
import gpigb.external.HUMSSystemImpl;
import gpigb.report.Reporter;
import gpigb.report.TestAppGUI;
import gpigb.sense.PortSensor;
import gpigb.sense.Sensor;
import gpigb.store.InMemoryStore;
import gpigb.store.Store;

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
//		TestAppGUI rState = new TestAppGUI();
		
		Integer aMeanID = aMgr.getModuleIDByName("gpigb.analyse.MeanAnalyser");
		
		Analyser aMean = (Analyser) aMgr.getObjectByID(aMgr.createObjectOfModule(aMeanID)).get();
		
		Integer sPort1ID = seMgr.getModuleIDByName("gpigb.sense.PortSensor");
		Sensor<Double> s1 = (Sensor<Double>) seMgr.getObjectByID(seMgr.createObjectOfModule(sPort1ID)).get();
		
		Integer stInMemID = stMgr.getModuleIDByName("gpigb.store.InMemoryStore");
		final Store st = stMgr.getObjectByID(stMgr.createObjectOfModule(stInMemID)).get();
		
		Integer rStateID = rMgr.getModuleIDByName("gpigb.report.TestAppGUI");
		Reporter rState = (Reporter) rMgr.getObjectByID(rMgr.createObjectOfModule(rStateID)).get();
		
		GUIConfigHandler configHandler = new GUIConfigHandler(aMgr.getAvailableObjects(), rMgr.getAvailableObjects(), stMgr.getAvailableObjects(), seMgr.getAvailableObjects());
		
		Map<String, ConfigurationValue> config;
		
		config = aMean.getConfigSpec();
		configHandler.getConfiguration(config);
		aMean.setConfig(config, null, null, null, stMgr);
		
		config = s1.getConfigSpec();
		configHandler.getConfiguration(config);
		s1.setConfig(config, null, null, null, null);
		s1.registerObserver(st);
		
		config = rState.getConfigSpec();
		configHandler.getConfiguration(config);
		rState.setConfig(config, aMgr, null, null, null);

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
