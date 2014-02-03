package gpigb.store;

import java.util.ArrayList;
import java.util.Collections;

import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;

public class InMemoryStore implements Store
{
	ArrayList<SensorRecord<?>> history = new ArrayList<>(); 
	
	@Override
	public boolean read(RecordSet<?> unpopulated)
	{		
		Collections.sort(history);
		
		int idx = 0;
		while(history.get(idx++).getTimestamp().before(unpopulated.getFromTime()));
		
		while(history.get(idx).getTimestamp().before(unpopulated.getToTime()))
		{
			if(history.get(idx).getSensorID() == unpopulated.getSensorID())
				unpopulated.addRecord((SensorRecord) history.get(idx));
			
			idx++;
		}
		
		return true;
	}

	@Override
	public boolean write(RecordSet<?> data)
	{
		int pos = 0;
		SensorRecord rec = null;
		while((rec = data.getReadingAtPosition(pos)) != null)
		{
			history.add(rec);
		}
		
		Collections.sort(history);
		
		return true;
	}

	@Override
	public boolean delete(RecordSet<?> items)
	{
		int pos = 0;
		SensorRecord rec = null;
		while((rec = items.getReadingAtPosition(pos)) != null)
		{
			history.remove(rec);
		}
		return true;
	}

}
