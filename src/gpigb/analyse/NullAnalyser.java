package gpigb.analyse;

import gpigb.data.RecordSet;
import gpigb.store.Store;

import java.util.List;

/**
 * An analyser which is used solely to acquire raw data from the store by reporter modules
 */
public class NullAnalyser implements Analyser{
	
	public Store store;
	
	@Override
	public boolean analyse(List<RecordSet<?>> input) {
		for(RecordSet data : input)
		{
			store.read(data);
		}
		return true;
	}

	@Override
	public boolean analyse(RecordSet<?> input) {
		store.read(input);
		return true;
	}
	
}
