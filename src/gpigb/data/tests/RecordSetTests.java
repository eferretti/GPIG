package gpigb.data.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecordSetTests
{
	RecordSet<Float> rs;
	Date fromTime;
	Date toTime;
	
	@Before
	public void setUp() throws Exception
	{
		fromTime = new Date();
		toTime = new Date(fromTime.getTime() + 100);
		rs = new RecordSet<>(fromTime, toTime, 0);
		fromTime = new Date();
	}

	@After
	public void tearDown() throws Exception
	{
		rs = null;
		System.gc();
	}
	
	@Test
	public void testEquals()
	{
		RecordSet<Float> nRs = new RecordSet<>(fromTime, toTime, 0);
		RecordSet<Float> nRs2 = new RecordSet<>(fromTime, toTime, 0);
		
		for (int i = 0; i < 10; i++)
		{
			SensorRecord<Float> newRec = new SensorRecord<Float>(0, 2.0f);
			nRs.addRecord(newRec);
			rs.addRecord(newRec);
			
			if (i%2 == 0)
				nRs2.addRecord(newRec);
		}
		
		assertEquals(rs.equals(nRs), true);
		assertEquals(rs.equals(nRs2), false);
	}
	
	@Test
	public void testAddRecord()
	{
		int initialSize = rs.getRecordCount();
		
		SensorRecord<Float> newRec = new SensorRecord<Float>(0, 2.0f);
		
		rs.addRecord(newRec);
		assertEquals(rs.getRecordCount(), initialSize+1);
		
		rs.addRecord(newRec);
		assertEquals(rs.getRecordCount(), initialSize+2);

		rs.addRecord(null);
		assertEquals(rs.getRecordCount(), initialSize+2);
	}

	@Test
	public void testGetReadingAtPosition()
	{
		SensorRecord<Float> newRec;
		for(int i = 0; i < 10; ++i)
		{
			newRec = new SensorRecord<Float>(0, i*2.0f);
			rs.addRecord(newRec);
		}
		
		assertEquals(rs.getReadingAtPosition(-1), null);
		assertEquals(rs.getReadingAtPosition(200), null);
		assertEquals(rs.getReadingAtPosition(2).getData().floatValue(), 2*2.0f, 0.001f);
	}

	@Test
	public void testGetRecordCount()
	{
		assertEquals(rs.getRecordCount(), 0);
		
		SensorRecord<Float> newRec = new SensorRecord<Float>(0, 2.0f);
		rs.addRecord(newRec);
		
		assertEquals(rs.getRecordCount(), 1);
	}

	@Test
	public void testGetFromTime()
	{
		assertNotSame(rs.getFromTime(), fromTime);
		assertEquals(rs.getFromTime().compareTo(fromTime), 0);
	}

	@Test
	public void testGetToTime()
	{
		assertNotSame(rs.getToTime(), toTime);
		assertEquals(rs.getToTime().compareTo(toTime), 0);
	}

	@Test
	public void testGetSensorID()
	{
		assertEquals(rs.getSensorID(), 0);
	}

}
