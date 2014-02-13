package gpigb.store.tests;

import static org.junit.Assert.assertEquals;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.store.JSONFileStore;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSONFileStoreTest
{
	JSONFileStore toBeTested = new JSONFileStore();
	RecordSet<Integer> rs, rs2, newRs;
	String filePath;
	Calendar cal = Calendar.getInstance();
	
	@Before
	public void setUp()
	{
		Date aDate = new Date();
		
		this.cal.setTime(aDate);
		this.cal.add(Calendar.MINUTE, 5);
		Date zDate = this.cal.getTime();
		
		this.rs = new RecordSet<>(aDate, zDate, 0);
		this.rs2 = new RecordSet<>(aDate, zDate, 0);
		
		this.cal.add(Calendar.MINUTE, -2);
		Date yDate = this.cal.getTime();
		this.newRs = new RecordSet<>(aDate, yDate, 0);
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
		int number = 50;
		this.cal.setTime(new Date());
		
		for (int i = 0; i < number; i++)
		{
			SensorRecord<Integer> newRecord = new SensorRecord<Integer>(0,42);
			this.cal.add(Calendar.SECOND, 6);
			newRecord.setDateTime(this.cal.getTime());
			this.rs.addRecord(newRecord);
		}
		
		this.toBeTested.write(this.rs);
		this.toBeTested.read(this.newRs);
		
		assertEquals(this.newRs.getRecordCount(), 30);
	}
	
	@Test
	public void testDelete()
	{
		int number = 50;
		this.cal.setTime(new Date());
		
		for (int i = 0; i < number; i++)
		{
			SensorRecord<Integer> newRecord = new SensorRecord<Integer>(0,42);
			this.cal.add(Calendar.SECOND, 6);
			newRecord.setDateTime(this.cal.getTime());
			this.rs.addRecord(newRecord);
		}
		
		this.toBeTested.write(this.rs);
		this.toBeTested.read(this.newRs);
		
		boolean success = this.toBeTested.delete(this.newRs);
		assertEquals(success, true);
		
		assertEquals(this.rs.getRecordCount(), number-this.newRs.getRecordCount());
	}
	
	@After
	public void tearDown()
	{
		File directory = new File("./Storage/0");
		File[] fileList = directory.listFiles();
		
		for (File file : fileList)
		{
			file.delete();
		}
		directory.delete();
		directory.getParentFile().delete();
	}
}
