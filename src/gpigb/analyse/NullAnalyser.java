package gpigb.analyse;

<<<<<<< HEAD
import gpigb.data.RecordSet;
=======
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.DataSet;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
import gpigb.store.Store;

import java.util.HashMap;
import java.util.List;

/**
 * An analyser which is used solely to acquire raw data from the store by reporter modules
 */
<<<<<<< HEAD
public class NullAnalyser implements Analyser{
	
	public Store store;
	
	@Override
	public boolean analyse(List<RecordSet<?>> input) {
		for(RecordSet data : input)
		{
			store.read(data);
		}
=======
public class NullAnalyser implements Analyser
{

	public StrongReference<Store> store;

	@SuppressWarnings("rawtypes")
	@Override
	public boolean analyse(List<DataSet<?>> input)
	{
		if(store == null || store.get() == null)
			return false;
		
		for(DataSet set : input)
			store.get().read(set);
		
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		return true;
	}

	@Override
<<<<<<< HEAD
	public boolean analyse(RecordSet<?> input) {
		store.read(input);
		return true;
	}
	
=======
	public boolean analyse(DataSet<?> input)
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

>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
}
