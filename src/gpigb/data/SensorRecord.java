package gpigb.data;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class SensorRecord<ReadingType> implements Comparable<SensorRecord<?>>
{
	private Date timestamp;
	private ReadingType data;
	private int sensorID;
	private Hashtable<String, String> meta;

	/**
	 * A record class for a single sensor reading.
	 * 
	 * @param sensorID
	 *            The ID of the sensor which made this reading
	 * @param data
	 *            The reading made by the sensor
	 * @param metaArgs
	 *            An array (or variable length list) of key, value pairs to be
	 *            stored as meta data
	 */
	public SensorRecord(int sensorID, ReadingType data, String... metaArgs)
	{
		this.sensorID = sensorID;
		this.data = data;
		this.meta = new Hashtable<>();
		for (int i = 0; i + 1 < metaArgs.length; i += 2) {
			this.meta.put(metaArgs[i], metaArgs[i + 1]);
		}
		this.timestamp = new Date();
	}

	/**
	 * Set the time that this data was recorded
	 * 
	 * @param newTime
	 *            The new time
	 */
	public void setDateTime(Date newTime)
	{
		this.timestamp = newTime;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @return the data
	 */
	public ReadingType getData()
	{
		return data;
	}

	/**
	 * @return the sensorID
	 */
	public int getSensorID()
	{
		return sensorID;
	}

	/**
	 * @return the meta
	 */
	public Dictionary<String, String> getMeta()
	{
		return meta;
	}

	@Override
	public int compareTo(SensorRecord<?> o)
	{
		return this.timestamp.compareTo(o.timestamp);
	}
	
	@Override
	public String toString()
	{
		return "" + data.toString() + " (d:" + timestamp.toString() + ", id:" + sensorID + ")\n";
	}
	
	
	
}
