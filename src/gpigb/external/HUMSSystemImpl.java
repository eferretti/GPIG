package gpigb.external;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager.InstanceSummary;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class HUMSSystemImpl implements HUMSSystem
{
	JarFileComponentManager<Analyser> aMgr = new JarFileComponentManager<>(Analyser.class);
	JarFileComponentManager<Reporter> rMgr = new JarFileComponentManager<>(Reporter.class);
	JarFileComponentManager<Sensor> seMgr = new JarFileComponentManager<>(Sensor.class);
	JarFileComponentManager<Store> stMgr = new JarFileComponentManager<>(Store.class);
	
	@Override
	public List<ModuleSummary> listAnalyserModules()
	{
		return aMgr.getAvailableModules();
	}

	@Override
	public int createAnalyser(int moduleID)
	{
		return aMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listAnalysers()
	{
		return aMgr.getAvailableObjects();
	}

	@Override
	public StrongReference<Analyser> getAnalyser(int id)
	{
		return aMgr.getObjectByID(id);
	}

	@Override
	public List<ModuleSummary> listReporterModules()
	{
		return rMgr.getAvailableModules();
	}

	@Override
	public int createReporter(int moduleID)
	{
		return rMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listReporters()
	{
		return rMgr.getAvailableObjects();
	}

	@Override
	public StrongReference<Reporter> getReporter(int id)
	{
		return rMgr.getObjectByID(id);
	}

	@Override
	public List<ModuleSummary> listSensorModules()
	{
		return seMgr.getAvailableModules();
	}

	@Override
	public int createSensor(int moduleID)
	{
		return seMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listSensors()
	{
		return seMgr.getAvailableObjects();
	}

	@Override
	public StrongReference<Sensor> getSensor(int id)
	{
		return seMgr.getObjectByID(id);
	}

	@Override
	public List<ModuleSummary> listStoreModules()
	{
		return stMgr.getAvailableModules();
	}

	@Override
	public int createStore(int moduleID)
	{
		return stMgr.createObjectOfModule(moduleID);
	}

	@Override
	public List<InstanceSummary> listStores()
	{
		return stMgr.getAvailableObjects();
	}

	@Override
	public StrongReference<Store> getStore(int id)
	{
		return stMgr.getObjectByID(id);
	}

	@Override
	public void uploadJarFile(InputStream uploadStream) throws IOException
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
		
		int read = -1;
		byte[] buffer = new byte[1024];
		while((read = uploadStream.read(buffer)) > 0)
		{
			fos.write(buffer, 0, read);
		}
		fos.flush();
		fos.close();
		
		aMgr.refreshModules();
		rMgr.refreshModules();
		seMgr.refreshModules();
		stMgr.refreshModules();
	}

}
