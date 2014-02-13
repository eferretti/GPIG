package gpigb.sense.tests;

import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.sense.ConcreteSensorOne;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SensorTest {
	@Before
	public void setUp()
	{
		Sensor<Integer> s1 = new ConcreteSensorOne();
		
		s1.registerObserver(new SensorObserver<Integer>()
				{
					@Override
					public void update(int sensorID, Integer reading)
					{
						
						RecordSet<Integer> rs = new RecordSet<>(new Date(), new Date(), sensorID);
						rs.addRecord(new SensorRecord<Integer>(1, reading));
					}

					@Override
					public void update(SensorRecord<Integer> reading) {
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
