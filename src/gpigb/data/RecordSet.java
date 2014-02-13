package gpigb.data;

import java.util.ArrayList;
import java.util.Collections;
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
		this.fromTime = new Date(from.getTime());
		this.toTime = new Date(to.getTime());
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
		if (data != null) this.data.add(data);
		Collections.sort(this.data);
	}

	/**
	 * Check that two instances of RecordSet are equal
	 * 
	 * @param rs
	 *            The RecordSet to compare to
	 * @return true if they match or false otherwise
	 */
	public boolean equals(RecordSet<DataType> rs)
	{
		if (rs == this) return true;
		if (this.fromTime.equals(rs.getFromTime()))
			if (this.toTime.equals(rs.getToTime()))
				if (this.sensorID == rs.getSensorID()) if (this.getRecordCount() == rs.getRecordCount()) {
					for (int i = 0; i < this.getRecordCount(); i++)
						if (this.getDataAtPosition(i).compareTo(rs.getDataAtPosition(i)) != 0) return false;
					return true;
				}
		return false;
	}

	/**
	 * Retrieve a specific record from the set
	 * 
	 * @param pos
	 *            The position of the record inside of the set
	 * @return the record
	 */
	public SensorRecord<DataType> getDataAtPosition(int pos)
	{
		if (pos < 0) return null;

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
		return new Date(fromTime.getTime());
	}

	/**
	 * @return the toTime
	 */
	public Date getToTime()
	{
		return new Date(toTime.getTime());
	}

	/**
	 * @return the sensorID
	 */
	public int getSensorID()
	{
		return sensorID;
	}
}
