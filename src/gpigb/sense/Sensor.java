package gpigb.sense;

public interface Sensor<DataType>
{
	public int getID();
	public DataType poll();
	public boolean isActive();
	
	public void registerObserver(SensorObserver<DataType> obs);
	public void removeObserver(SensorObserver<DataType> obs);
	public void notifyObservers();
}
