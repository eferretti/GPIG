package gpigb.sense;

import gpigb.data.SensorRecord;

public interface SensorObserver
{
	/**
	 * Returns the new data to the observers after they have been notified
	 * 
	 * @param sensorID
	 *            the ID of the sensor
	 * @param reading
	 *            the new data
	 */
	public boolean update(int sensorID, Integer reading);
	public boolean update(int sensorID, Double reading);
	public boolean update(int sensorID, String reading);
	
	public boolean update(SensorRecord<?> reading);
}
