package gpigb.report.tests;

import static org.junit.Assert.*;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.report.Reporter1;
import gpigb.report.Reporter2;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReporterTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	
	@Before
	public void setUp()
	{
		System.setOut(new PrintStream(outContent));
	}
	
	@Test
	public void testReporter1()
	{
		List<RecordSet<?>> errorData = new ArrayList<>();
		Reporter report = new Reporter1();

		int id = 1;
		Integer reading = 12;
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1980);
		Date f = c.getTime();
		c.set(Calendar.YEAR, 5000);
		Date t = c.getTime();
		
		SensorRecord<Integer> sr = new SensorRecord<Integer>(id, reading);
		Date timestamp = sr.getTimestamp();
		
		RecordSet<Integer> rs = new RecordSet<Integer>(f, t, 1);
		rs.addRecord(sr);
		errorData.add(rs);
		report.generateReport(errorData);
		
	    assertEquals("SensorID: " + Integer.toString(id) + " Time: " + String.valueOf(timestamp) + " Reading: "+ Integer.toString(reading) + "\n", outContent.toString());
	}
	
	@Test
	public void testReporter2()
	{
		List<RecordSet<?>> errorData = new ArrayList<>();
		Reporter report = new Reporter2();

		int id = 1;
		Integer reading = 12;
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1980);
		Date f = c.getTime();
		c.set(Calendar.YEAR, 5000);
		Date t = c.getTime();
		
		SensorRecord<Integer> sr = new SensorRecord<Integer>(id, reading);
		Date timestamp = sr.getTimestamp();
		
		RecordSet<Integer> rs = new RecordSet<Integer>(f, t, 1);
		rs.addRecord(sr);
		errorData.add(rs);
		report.generateReport(errorData);
		
	    assertEquals("SensorID: " + Integer.toString(id) + " Time: " + String.valueOf(timestamp) + " Reading: "+ Integer.toString(reading) + "\n", outContent.toString());
	}

	
	@After
	public void tearDown()
	{
		System.setOut(null);
	}
}