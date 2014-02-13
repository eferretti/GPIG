package gpigb.analyse;

import gpigb.data.DataSet;
import gpigb.store.Store;

import java.util.List;

/**
 * An analyser which is used solely to acquire raw data from the store by
 * reporter modules
 */
public class NullAnalyser implements Analyser
{

	public Store store;

	@Override
	public boolean analyse(List<DataSet<?>> input)
	{
		return true;
	}

	@Override
	public boolean analyse(DataSet<?> input)
	{
		return true;
	}

}
