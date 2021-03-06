package gpigb.store;

import gpigb.configuration.Configurable;
import gpigb.data.RecordSet;
import gpigb.sense.SensorObserver;

/**
 * Common methods between all store implementations
 */
public interface Store extends Configurable, SensorObserver
{
	/**
	 * Fills a provided record set with values from the store
	 * 
	 * @param unpopulated
	 *            A {@link gpigb.data.RecordSet} object with the Date and sensor
	 *            IDs set
	 * @return true iff the record set has been populated without error
	 */
	public boolean read(RecordSet<?> unpopulated);

	/**
	 * Persists a set of records to the store
	 * 
	 * @param data
	 *            The data to store
	 * @return true iff all data item are saved without error
	 */
	public boolean write(RecordSet<?> data);

	/**
	 * Delete all records in the set
	 * 
	 * @param items
	 *            The list of items to be removed
	 * @return true iff all items are removed successfully
	 */
	public boolean delete(RecordSet<?> items);
}
