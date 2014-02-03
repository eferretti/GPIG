package gpigb.sense;

public interface SensorObserver<DataType>
{
	/**
	 * Returns the new data to the observers after they have been notified
	 * @param sensorID the ID of the sensor
	 * @param reading the new data
	 */
	public void update(int sensorID, DataType reading);
}