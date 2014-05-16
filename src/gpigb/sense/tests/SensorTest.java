package gpigb.sense.tests;

import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
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
		Map<String, ConfigurationValue> configSpec = s1.getConfigSpec();
		configSpec.put("Min", new ConfigurationValue(ValueType.Integer, -5000));
		configSpec.put("Max", new ConfigurationValue(ValueType.Integer, 20000));
		s1.setConfig(configSpec, null, null, null, null);
		
		s1.registerObserver(new SensorObserver()
		{
			@Override
			public boolean update(int sensorID, Integer reading)
			{

				RecordSet<Integer> rs = new RecordSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new SensorRecord<Integer>(1, reading));
				return true;
			}

			@Override
			public boolean update(SensorRecord<?> reading)
			{
				return false;

			}

			@Override
			public boolean update(int sensorID, Double reading) {
				return false;
				
			}

			@Override
			public boolean update(int sensorID, String reading) {
				return false;
				
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
