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

	/**
	 * A record set that contains multiple sensor readings
	 * 
	 * @param from
	 *            A Date that marks the beginning timestamp of the data
	 * @param to
	 *            A Date that marks the end timestamp of the data
	 * @param sensor
	 *            The specific sensor ID that the data relates to
	 */
	public RecordSet(Date from, Date to, int sensor)
	{
		this.fromTime = from;
		this.toTime = to;
		this.sensorID = sensor;
		this.data = new ArrayList<>();
	}

	/**
	 * Add a single record in to the record set
	 * 
	 * @param data
	 *            The data to be added to the set
	 */
	public void addRecord(SensorRecord<DataType> data)
	{
		this.data.add(data);
	}

	/**
	 * Retrieve a specific record from the set
	 * 
	 * @param pos
	 *            The position of the record inside of the set
	 * @return the record
	 */
	public SensorRecord<DataType> getReadingAtPosition(int pos)
	{
		if (this.data.size() > pos) return this.data.get(pos);

		return null;
	}

	/**
	 * Return the size of the data set
	 * 
	 * @return the size
	 */
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
