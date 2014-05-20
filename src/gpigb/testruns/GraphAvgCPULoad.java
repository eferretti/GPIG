package gpigb.testruns;

import gpigb.analyse.Analyser;
import gpigb.analyse.NullAnalyser;
import gpigb.analyse.RTNullAnalyser;
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

public class GraphAvgCPULoad {
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
			

			
		Integer analID = aMgr.getModuleIDByName("gpigb.analyse.RTNullAnalyser");	
		Analyser aRTNull = (Analyser) aMgr.getObjectByID(aMgr.createObjectOfModule(analID)).get();
		
		Integer cpuSensID = seMgr.getModuleIDByName("gpigb.sense.averageSystemCPULoadSensor");
		Sensor<Double> cpuSens = (Sensor<Double>) seMgr.getObjectByID(seMgr.createObjectOfModule(cpuSensID)).get();
		
		Integer storID = stMgr.getModuleIDByName("gpigb.store.InMemoryStore");
		Store storMem = stMgr.getObjectByID(stMgr.createObjectOfModule(storID)).get();
		
		Integer repID = rMgr.getModuleIDByName("gpigb.report.ReporterPlotRTSmart");
		Reporter repGraph = (Reporter) rMgr.getObjectByID(rMgr.createObjectOfModule(repID)).get();
		
		GUIConfigHandler configHandler = new GUIConfigHandler(aMgr.getAvailableObjects(), rMgr.getAvailableObjects(), stMgr.getAvailableObjects(), seMgr.getAvailableObjects());
		
		Map<String, ConfigurationValue> config;		
		config = cpuSens.getConfigSpec();
		configHandler.getConfiguration(config);
		cpuSens.setConfig(config, null, null, null, null);
		
			
		config = repGraph.getConfigSpec();
		configHandler.getConfiguration(config);
		repGraph.setConfig(config, aMgr, null, seMgr, null);
		
		config = aRTNull.getConfigSpec();
		configHandler.getConfiguration(config);
		aRTNull.setConfig(config, null, rMgr, null, stMgr);
		
		cpuSens.registerObserver(storMem);
		cpuSens.registerObserver((SensorObserver) aRTNull);
		
		while (true) {	
			cpuSens.poll();
			try {
				Thread.sleep(1000);
			}
			catch(Exception e){}
		}
	}
}
