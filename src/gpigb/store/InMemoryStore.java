package gpigb.store;

import gpigb.classloading.Patchable;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;
import gpigb.sense.SensorObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A record store which maintains all records in memory. This should not be used
 * as the sole store of a system and should only be used to provide faster
 * access when only a slow store is available
 * 
 * @param <DataType>
 */
public class InMemoryStore<DataType> extends Patchable implements Store, SensorObserver<DataType>
{
	List<SensorRecord<?>> history = Collections.synchronizedList(new ArrayList<SensorRecord<?>>());

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
	public boolean read(RecordSet<?> unpopulated)
	{
		if (history.size() <= 0) return false;

		int idx = 0;
		while (idx < history.size() && history.get(idx++).getTimestamp().before(unpopulated.getFromTime()));

		while (idx < history.size() && history.get(idx).getTimestamp().before(unpopulated.getToTime())) {
			if (history.get(idx).getSensorID() == unpopulated.getSensorID())
				unpopulated.addRecord((SensorRecord) history.get(idx));

			idx++;
		}

		return true;
	}

	@SuppressWarnings(
	{ "rawtypes" })
	@Override
	public boolean write(RecordSet<?> data)
	{
		int pos = 0;
		SensorRecord rec = null;
		while ((rec = data.getDataAtPosition(pos++)) != null) {
			history.add(rec);
		}

//		System.out.println("Added " + pos + " new objects");

		Collections.sort(history);

//		for (SensorRecord<?> r : history) {
//			System.out.println(r.getTimestamp());
//		}

		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean delete(RecordSet<?> items)
	{
		int pos = 0;
		SensorRecord rec = null;
		while ((rec = items.getDataAtPosition(pos++)) != null) {
			history.remove(rec);
		}
		return true;
	}

	@Override
	public void update(int sensorID, DataType reading)
	{
		Date date = Calendar.getInstance().getTime();
		RecordSet<DataType> rs = new RecordSet<>(date, date, sensorID);
		rs.addRecord(new SensorRecord<DataType>(sensorID, reading));
		write(rs);
	}

	@Override
	public void update(SensorRecord<DataType> reading)
	{
		RecordSet<DataType> rs = new RecordSet<>(reading.getTimestamp(), reading.getTimestamp(), reading.getSensorID());
		rs.addRecord(reading);
		write(rs);
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		return null;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec)
	{
		return true;
	}

}
