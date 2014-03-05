package gpigb.store;

import gpigb.classloading.Patchable;
import gpigb.configuration.ConfigurationHandler;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
import gpigb.sense.SensorObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A record store which maintains all records in memory. This should not be used
 * as the sole store of a system and should only be used to provide faster
 * access when only a slow store is available
 * 
 * @param <DataType>
 */
public class InMemoryStore<DataType> extends Patchable implements Store, SensorObserver<DataType>
{
	List<DataRecord<?>> history = Collections.synchronizedList(new ArrayList<DataRecord<?>>());

	public static final int VERSION_NUMBER = 2;

	public InMemoryStore()
	{
		super();
	}

	public InMemoryStore(Object oldInstance)
	{
		super(oldInstance);
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	@Override
	public boolean read(DataSet<?> unpopulated)
	{
		if (history.size() <= 0) return false;

		int idx = 0;
		while (idx < history.size() && history.get(idx++).getTimestamp().before(unpopulated.getFromTime()));

		while (idx < history.size() && history.get(idx).getTimestamp().before(unpopulated.getToTime())) {
			if (history.get(idx).getSensorID() == unpopulated.getSensorID())
				unpopulated.addRecord((DataRecord) history.get(idx));

			idx++;
		}

		return true;
	}

	@SuppressWarnings(
	{ "rawtypes" })
	@Override
	public boolean write(DataSet<?> data)
	{
		int pos = 0;
		DataRecord rec = null;
		while ((rec = data.getDataAtPosition(pos++)) != null) {
			history.add(rec);
		}

//		System.out.println("Added " + pos + " new objects");

		Collections.sort(history);

//		for (DataRecord<?> r : history) {
//			System.out.println(r.getTimestamp());
//		}

		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean delete(DataSet<?> items)
	{
		int pos = 0;
		DataRecord rec = null;
		while ((rec = items.getDataAtPosition(pos++)) != null) {
			history.remove(rec);
		}
		return true;
	}

	@Override
	public void update(int sensorID, DataType reading)
	{
		Date date = Calendar.getInstance().getTime();
		DataSet<DataType> rs = new DataSet<>(date, date, sensorID);
		rs.addRecord(new DataRecord<DataType>(sensorID, reading));
		write(rs);
	}

	@Override
	public void update(DataRecord<DataType> reading)
	{
		DataSet<DataType> rs = new DataSet<>(reading.getTimestamp(), reading.getTimestamp(), reading.getSensorID());
		rs.addRecord(reading);
		write(rs);
	}

	@Override
	public void configure(ConfigurationHandler handler)
	{
		// TODO Auto-generated method stub
		
	}

}
