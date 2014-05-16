package gpigb.analyse;

import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An analyser which is used solely to acquire raw data from the store by reporter modules
 */
public class NullAnalyser implements Analyser
{

	public StrongReference<Store> store;

	@SuppressWarnings("rawtypes")
	@Override
	public boolean analyse(List<RecordSet<?>> input)
	{
		if(store == null || store.get() == null)
			return false;
		
		for(RecordSet set : input)
			store.get().read(set);
		
		return true;
	}

	@Override
	public boolean analyse(RecordSet<?> input)
	{
		if(store == null || store.get() == null)
			return false;
		
		store.get().read(input);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Store", new ConfigurationValue(ValueType.Store, 0));
		return configMap;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.store = stMgr.getObjectByID(newSpec.get("Store").intValue);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private int id;
	public void setID(int newID)
	{
		this.id = newID;
	}
	
	public int getID()
	{
		return this.id;
	}
}
