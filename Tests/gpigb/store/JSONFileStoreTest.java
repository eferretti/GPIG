package gpigb.store;

import static org.junit.Assert.assertEquals;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.sense.Sensor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class JSONFileStoreTest
{
	JSONFileStore toBeTested = new JSONFileStore();
	
	@Test
	public void testWrite()
	{
		Calendar cal = Calendar.getInstance();
		Date aDate = new Date();
		
		cal.setTime(aDate);
		cal.add(Calendar.HOUR, 1);
		Date zDate = cal.getTime();
		
		RecordSet<Integer> rs = new RecordSet<>(aDate, zDate, 0);		
		
		for (int i = 0; i < 100; i++)
		{
			rs.addRecord(new SensorRecord<Integer>(0,42));
		}
		
		assertEquals(this.toBeTested.write(rs),true);
		
		String filePath = "./Storage/0/" + aDate.getTime() + "-" + zDate.getTime() + ".json";
		File file = new File(filePath);
		
		assertEquals(file.exists(), true);
	}
	
	@Test
	public void testRead()
	{
		Calendar cal = Calendar.getInstance();
		Date aDate = new Date();
		
		cal.setTime(aDate);
		cal.add(Calendar.HOUR, 1);
		Date zDate = cal.getTime();
		
		RecordSet<Integer> rs = new RecordSet<>(aDate, zDate, 0);		
		
		for (int i = 0; i < 100; i++)
		{
			rs.addRecord(new SensorRecord<Integer>(0,42));
		}
		
		this.toBeTested.write(rs);
		
		RecordSet<Integer> newRs = new RecordSet<>(aDate, zDate, 0);
		this.toBeTested.read(newRs);
		System.out.println(newRs.getRecordCount());
		assertEquals(newRs.getRecordCount(), 100);
	}
}
