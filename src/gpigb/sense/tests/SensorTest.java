package gpigb.sense.tests;

<<<<<<< HEAD
import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;
import gpigb.sense.ConcreteSensorOne;
=======
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
import gpigb.sense.RandomValueSensor;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;

import java.util.Date;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SensorTest
{
	@Before
	public void setUp()
	{
		Sensor<Integer> s1 = new RandomValueSensor();
		s1.configure(new ConfigurationHandler()
		{
			
			@Override
			public void getConfiguration(Map<String, ConfigurationValue> configSpec)
			{
				configSpec.put("Min", new ConfigurationValue(ValueType.Integer, -5000));
				configSpec.put("Max", new ConfigurationValue(ValueType.Integer, 20000));
			}
		});
		
		s1.registerObserver(new SensorObserver<Integer>()
		{
			@Override
			public void update(int sensorID, Integer reading)
			{

				RecordSet<Integer> rs = new RecordSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new SensorRecord<Integer>(1, reading));
			}

			@Override
			public void update(SensorRecord<Integer> reading)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	@Test
	public void testDataTransfer()
	{
		// TODO
	}

	@After
	public void tearDown()
	{

	}
}
