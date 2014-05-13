package gpigb.analyse;

import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.store.Store;

import java.util.HashMap;
import java.util.List;

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
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Store", new ConfigurationValue(ValueType.Store, null));
		handler.getConfiguration(configMap);
		store = (StrongReference<Store>) configMap.get("Store").value;
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
