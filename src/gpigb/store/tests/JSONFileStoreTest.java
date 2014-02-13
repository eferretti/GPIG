package gpigb.store.tests;

import static org.junit.Assert.assertEquals;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.store.JSONFileStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSONFileStoreTest
{
	JSONFileStore toBeTested = new JSONFileStore();
	String filePath = "./Storage/0/";
	Date a, b, e, f;
	
	@Before
	public void setUp()
	{
		this.a = new Date(new GregorianCalendar(2013,12,1,1,0,0).getTime().getTime());
		this.b = new Date(new GregorianCalendar(2013,12,1,1,5,0).getTime().getTime());
		this.e = new Date(new GregorianCalendar(2013,12,1,1,10,0).getTime().getTime());
		this.f = new Date(new GregorianCalendar(2013,12,1,1,15,0).getTime().getTime());
	}
	
	@Test
	public void testWrite()
	{
		RecordSet<Integer> rs1 = new RecordSet<>(a, b, 0);
		
		for (int i = 0; i < 20; i++)
		{
			SensorRecord<Integer> s = new SensorRecord<Integer>(0,42);
			s.setDateTime(a);
			rs1.addRecord(s);
		}
		
		assertEquals(this.toBeTested.write(rs1),true);
		File file = new File(this.filePath + a.getTime() + "-" + b.getTime() + ".json");
		
		assertEquals(file.exists(), true);
		
		String fileContents = "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
							+"{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}";
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			
			while ((line = reader.readLine()) != null)
			{
				stringBuilder.append(line);
			}
			
			reader.close();
			assertEquals(stringBuilder.toString().equals(fileContents), true);
		}
		catch (IOException e)
		{
			// failure
			assertEquals(true,false);
		}
	}
	
	@Test
	public void testRead()
	{
		RecordSet<Integer> rs1 = new RecordSet<>(a, b, 0);
		RecordSet<Integer> rs2 = new RecordSet<>(b, e, 0);
		RecordSet<Integer> rs3 = new RecordSet<>(e, f, 0);
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(a);
		cal.add(Calendar.MINUTE, 3);
		
		Date x = cal.getTime();
		
		cal.setTime(f);
		cal.add(Calendar.MINUTE, -2);
		
		Date y = cal.getTime();
		
		RecordSet<Integer> readin = new RecordSet<>(x, y, 0);
		RecordSet<Integer> readinCheck = new RecordSet<>(x, y, 0);
		
		cal.setTime(a);
		
		for (int i = 0; i < 5; i++)
		{
			SensorRecord<Integer> newRecord = new SensorRecord<Integer>(0,42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs1.addRecord(newRecord);
			if (i > 2)
				readinCheck.addRecord(newRecord);
		}
		
		for (int i = 0; i < 5; i++)
		{
			SensorRecord<Integer> newRecord = new SensorRecord<Integer>(0,42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs2.addRecord(newRecord);
			readinCheck.addRecord(newRecord);
		}
		
		for (int i = 0; i < 5; i++)
		{
			SensorRecord<Integer> newRecord = new SensorRecord<Integer>(0,42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs3.addRecord(newRecord);
			if (i < 4)
				readinCheck.addRecord(newRecord);
		}
		
		this.toBeTested.write(rs1);
		this.toBeTested.write(rs2);
		this.toBeTested.write(rs3);
		
		this.toBeTested.read(readin);

		assertEquals(readin.getRecordCount(), 11);
		assertEquals(readin.equals(readinCheck), true);
	}
	
	/*@Test
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
		
		System.out.println(this.toBeTested.read(this.rs2));
		
		System.out.println(this.rs2.getRecordCount());
		assertEquals(this.rs2.getRecordCount(), 50-this.newRs.getRecordCount());
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
	}*/
}
