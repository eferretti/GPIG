package gpigb.store;

import gpigb.classloading.Patchable;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.sense.SensorObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean read(RecordSet<?> unpopulated)
	{
		if(history.size() <= 0)
			return false;
		
		int idx = 0;
		while (idx < history.size() && history.get(idx++).getTimestamp()
				.before(unpopulated.getFromTime()))
			;

		while (idx < history.size() && history.get(idx).getTimestamp().before(unpopulated.getToTime()))
		{
			if (history.get(idx).getSensorID() == unpopulated.getSensorID())
				unpopulated.addRecord((SensorRecord) history.get(idx));

			idx++;
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean write(RecordSet<?> data)
	{
		int pos = 0;
		SensorRecord rec = null;
		while ((rec = data.getReadingAtPosition(pos++)) != null)
		{
			history.add(rec);
		}

		System.out.println("Added " + pos + " new objects");
		
		Collections.sort(history);

		for(SensorRecord<?> r : history)
		{
			System.out.println(r.getTimestamp());
		}
		
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean delete(RecordSet<?> items)
	{
		int pos = 0;
		SensorRecord rec = null;
		while ((rec = items.getReadingAtPosition(pos++)) != null)
		{
			history.remove(rec);
		}
		return true;
	}

	@Override
	public void update(int sensorID, DataType reading) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(SensorRecord<DataType> reading) {
		// TODO Auto-generated method stub
		
	}

}
