package gpigb.sense.tests;

import gpigb.configuration.ConfigurationHandler;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
import gpigb.sense.RandomValueSensor;
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
			public void getConfiguration(Map<String, Object> configSpec)
			{
				configSpec.put("Min", -5000);
				configSpec.put("Max", 20000);
			}
		});
		
		s1.registerObserver(new SensorObserver<Integer>()
		{
			@Override
			public void update(int sensorID, Integer reading)
			{

				DataSet<Integer> rs = new DataSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new DataRecord<Integer>(1, reading));
			}

			@Override
			public void update(DataRecord<Integer> reading)
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
