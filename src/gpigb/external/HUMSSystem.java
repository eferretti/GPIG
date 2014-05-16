package gpigb.external;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager.InstanceSummary;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.report.Reporter;
import gpigb.store.Store;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface HUMSSystem extends Remote
{
	public List<ModuleSummary> listAnalyserModules() throws RemoteException;
	public int createAnalyser(int moduleID) throws RemoteException;
	public List<InstanceSummary> listAnalysers() throws RemoteException;
	public StrongReference<Analyser> getAnalyser(int id) throws RemoteException;

	public List<ModuleSummary> listReporterModules() throws RemoteException;
	public int createReporter(int moduleID) throws RemoteException;
	public List<InstanceSummary> listReporters() throws RemoteException;
	public StrongReference<Reporter> getReporter(int id) throws RemoteException;

	public List<ModuleSummary> listSensorModules() throws RemoteException;
	public int createSensor(int moduleID) throws RemoteException;
	public List<InstanceSummary> listSensors() throws RemoteException;
	public Map<String, ConfigurationValue> getSensorConfig(int id) throws RemoteException;
	public boolean setSensorConfig(int id, Map<String, ConfigurationValue> newConfig) throws RemoteException;

	public List<ModuleSummary> listStoreModules() throws RemoteException;
	public int createStore(int moduleID) throws RemoteException;
	public List<InstanceSummary> listStores() throws RemoteException;
	public StrongReference<Store> getStore(int id) throws RemoteException;
	
	public void uploadJarFile(byte[] contents) throws IOException, RemoteException;
}
