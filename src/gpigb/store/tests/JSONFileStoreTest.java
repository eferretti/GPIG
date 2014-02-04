package gpigb.store.tests;

import static org.junit.Assert.assertEquals;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.sense.Sensor;
import gpigb.store.JSONFileStore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSONFileStoreTest
{
	JSONFileStore toBeTested = new JSONFileStore();
	RecordSet<Integer> rs, newRs;
	String filePath;
	
	@Before
	public void setUp()
	{
		Calendar cal = Calendar.getInstance();
		Date aDate = new Date();
		
		cal.setTime(aDate);
		cal.add(Calendar.HOUR, 1);
		Date zDate = cal.getTime();
		
		this.rs = new RecordSet<>(aDate, zDate, 0);
		this.newRs = new RecordSet<>(aDate, zDate, 0);
		this.filePath = "./Storage/0/" + aDate.getTime() + "-" + zDate.getTime() + ".json";
	}
	
	@Test
	public void testWrite()
	{
		for (int i = 0; i < 100; i++)
		{
			this.rs.addRecord(new SensorRecord<Integer>(0,42));
		}
		
		assertEquals(this.toBeTested.write(this.rs),true);
		File file = new File(this.filePath);
		
		assertEquals(file.exists(), true);
	}
	
	@Test
	public void testRead()
	{
		for (int i = 0; i < 100; i++)
		{
			this.rs.addRecord(new SensorRecord<Integer>(0,42));
		}
		
		this.toBeTested.write(this.rs);
		
		this.toBeTested.read(this.newRs);
		System.out.println(this.newRs.getRecordCount());
		assertEquals(this.newRs.getRecordCount(), 100);
	}
	
	@After
	public void tearDown()
	{
		File file = new File(this.filePath);
		file.delete();
	}
}
