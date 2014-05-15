package gpigb.external;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager.InstanceSummary;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HUMSSystemImpl extends UnicastRemoteObject implements HUMSSystem
{
	JarFileComponentManager<Analyser> aMgr = new JarFileComponentManager<>(Analyser.class);
	JarFileComponentManager<Reporter> rMgr = new JarFileComponentManager<>(Reporter.class);
	JarFileComponentManager<Sensor> seMgr = new JarFileComponentManager<>(Sensor.class);
	JarFileComponentManager<Store> stMgr = new JarFileComponentManager<>(Store.class);
	
	public HUMSSystemImpl() throws RemoteException
	{
		super(0);
		aMgr.addModuleDirectory("./Modules/");
		rMgr.addModuleDirectory("./Modules/");
		seMgr.addModuleDirectory("./Modules/");
		stMgr.addModuleDirectory("./Modules/");
	}
	
	@Override
	public List<ModuleSummary> listAnalyserModules() throws RemoteException
	{
		return aMgr.getAvailableModules();
	}

	@Override
	public int createAnalyser(int moduleID) throws RemoteException
	{
		return aMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listAnalysers() throws RemoteException
	{
		return aMgr.getAvailableObjects();
	}

	@Override
	public StrongReference<Analyser> getAnalyser(int id) throws RemoteException
	{
		return aMgr.getObjectByID(id);
	}

	@Override
	public List<ModuleSummary> listReporterModules() throws RemoteException
	{
		return rMgr.getAvailableModules();
	}

	@Override
	public int createReporter(int moduleID) throws RemoteException
	{
		return rMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listReporters() throws RemoteException
	{
		return rMgr.getAvailableObjects();
	}

	@Override
	public StrongReference<Reporter> getReporter(int id) throws RemoteException
	{
		return rMgr.getObjectByID(id);
	}

	@Override
	public List<ModuleSummary> listSensorModules() throws RemoteException
	{
		seMgr.refreshModules();
		return seMgr.getAvailableModules();
	}

	@Override
	public int createSensor(int moduleID) throws RemoteException
	{
		return seMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listSensors() throws RemoteException
	{
		return seMgr.getAvailableObjects();
	}

	@Override
	public Map<String, ConfigurationValue> getSensorConfig(int id) throws RemoteException
	{
		return seMgr.getObjectByID(id).get().getConfigSpec();
	}

	@Override
	public boolean setSensorConfig(int id, Map<String, ConfigurationValue> newConfig) throws RemoteException
	{
		return seMgr.getObjectByID(id).get().setConfig(newConfig, null, null, null, null);
	}

	@Override
	public List<ModuleSummary> listStoreModules() throws RemoteException
	{
		return stMgr.getAvailableModules();
	}

	@Override
	public int createStore(int moduleID) throws RemoteException
	{
		return stMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listStores() throws RemoteException
	{
		return stMgr.getAvailableObjects();
	}

	@Override
	public StrongReference<Store> getStore(int id) throws RemoteException
	{
		return stMgr.getObjectByID(id);
	}

	@Override
	public void uploadJarFile(byte[] fileContent) throws IOException, RemoteException
	{	
		String name = "./Modules/";
		Random r = new Random();
		for(int i = 0; i < 25; ++i)
		{
			if(i > 0 && i%5 == 0)
				name += "-";
			
			name += (char)(r.nextInt(26) + (int)'a');
		}
		name += ".jar";
		
		File saveFile = new File(name);
		saveFile.getParentFile().mkdirs();
		
		FileOutputStream fos = new FileOutputStream(saveFile);
		
		fos.write(fileContent, 0, fileContent.length);

		fos.flush();
		fos.close();
		
		aMgr.refreshModules();
		rMgr.refreshModules();
		seMgr.refreshModules();
		stMgr.refreshModules();
	}

}
