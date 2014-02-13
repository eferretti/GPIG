package gpigb.data.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecordSetTests
{
	DataSet<Float> rs;
	Date fromTime;
	Date toTime;

	@Before
	public void setUp() throws Exception
	{
		fromTime = new Date();
		toTime = new Date(fromTime.getTime() + 100);
		rs = new DataSet<>(fromTime, toTime, 0);
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
		DataSet<Float> nRs = new DataSet<>(fromTime, toTime, 0);
		DataSet<Float> nRs2 = new DataSet<>(fromTime, toTime, 0);

		for (int i = 0; i < 10; i++) {
			DataRecord<Float> newRec = new DataRecord<Float>(0, 2.0f);
			nRs.addRecord(newRec);
			rs.addRecord(newRec);

			if (i % 2 == 0) nRs2.addRecord(newRec);
		}

		assertEquals(rs.equals(nRs), true);
		assertEquals(rs.equals(nRs2), false);
	}

	@Test
	public void testAddRecord()
	{
		int initialSize = rs.getRecordCount();

		DataRecord<Float> newRec = new DataRecord<Float>(0, 2.0f);

		rs.addRecord(newRec);
		assertEquals(rs.getRecordCount(), initialSize + 1);

		rs.addRecord(newRec);
		assertEquals(rs.getRecordCount(), initialSize + 2);

		rs.addRecord(null);
		assertEquals(rs.getRecordCount(), initialSize + 2);
	}

	@Test
	public void testGetReadingAtPosition()
	{
		DataRecord<Float> newRec;
		for (int i = 0; i < 10; ++i) {
			newRec = new DataRecord<Float>(0, i * 2.0f);
			rs.addRecord(newRec);
		}

		assertEquals(rs.getDataAtPosition(-1), null);
		assertEquals(rs.getDataAtPosition(200), null);
		assertEquals(rs.getDataAtPosition(2).getData().floatValue(), 2 * 2.0f, 0.001f);
	}

	@Test
	public void testGetRecordCount()
	{
		assertEquals(rs.getRecordCount(), 0);

		DataRecord<Float> newRec = new DataRecord<Float>(0, 2.0f);
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
