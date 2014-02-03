package gpigb.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordSet<DataType>
{
	private Date fromTime;
	private Date toTime;
	private int sensorID;
	private List<SensorRecord<DataType>> data;

	public RecordSet(Date from, Date to, int sensor)
	{
		this.fromTime = from;
		this.toTime = to;
		this.sensorID = sensor;
		this.data = new ArrayList<>();
	}
	
	public void addRecord(SensorRecord<DataType> data)
	{
		this.data.add(data);
	}
	
	public SensorRecord<DataType> getReadingAtPosition(int pos)
	{
		if(this.data.size() < pos)
			return this.data.get(pos);
		
		return null;
	}
	
	public int getRecordCount()
	{
		return this.data.size();
	}

	/**
	 * @return the fromTime
	 */
	public Date getFromTime()
	{
		return fromTime;
	}

	/**
	 * @return the toTime
	 */
	public Date getToTime()
	{
		return toTime;
	}

	/**
	 * @return the sensorID
	 */
	public int getSensorID()
	{
		return sensorID;
	}
	
	
}
