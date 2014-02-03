package gpigb.sense;

public interface SensorObserver<DataType>
{
	public void update(int sensorID, DataType reading);
}