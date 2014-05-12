package gpigb.sense;

import gpigb.configuration.Configurable;

public interface Sensor<DataType> extends Configurable
{
	/**
	 * Return the sensor unique ID
	 * 
	 * @return the ID
	 */
	public int getID();

	/**
	 * Retrieves the last sensor reading
	 * 
	 * @return a sensor reading
	 */
	public DataType poll();

	/**
	 * Check that the sensor is initialised and on
	 * 
	 * @return sensor status
	 */
	public boolean isActive();

	/**
	 * Register an observer with the sensor
	 * 
	 * @param obs
	 *            the observer
	 */
	public void registerObserver(SensorObserver<DataType> obs);

	/**
	 * Remove an observer from the sensor
	 * 
	 * @param obs
	 *            the observer
	 */
	public void removeObserver(SensorObserver<DataType> obs);

	/**
	 * Notify all observers that new data is available
	 */
	public void notifyObservers();
}
