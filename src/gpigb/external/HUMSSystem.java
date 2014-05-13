package gpigb.external;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager.InstanceSummary;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.classloading.StrongReference;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface HUMSSystem
{
	public List<ModuleSummary> listAnalyserModules();
	public int createAnalyser(int moduleID);
	public List<InstanceSummary> listAnalysers();
	public StrongReference<Analyser> getAnalyser(int id);

	public List<ModuleSummary> listReporterModules();
	public int createReporter(int moduleID);
	public List<InstanceSummary> listReporters();
	public StrongReference<Reporter> getReporter(int id);

	public List<ModuleSummary> listSensorModules();
	public int createSensor(int moduleID);
	public List<InstanceSummary> listSensors();
	public StrongReference<Sensor> getSensor(int id);

	public List<ModuleSummary> listStoreModules();
	public int createStore(int moduleID);
	public List<InstanceSummary> listStores();
	public StrongReference<Store> getStore(int id);
	
	public void uploadJarFile(InputStream uploadStream) throws IOException;
}
