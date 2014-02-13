package gpigb.store.tests;

import static org.junit.Assert.assertEquals;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
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
		this.a = new Date(new GregorianCalendar(2013, 12, 1, 1, 0, 0).getTime().getTime());
		this.b = new Date(new GregorianCalendar(2013, 12, 1, 1, 5, 0).getTime().getTime());
		this.e = new Date(new GregorianCalendar(2013, 12, 1, 1, 10, 0).getTime().getTime());
		this.f = new Date(new GregorianCalendar(2013, 12, 1, 1, 15, 0).getTime().getTime());
	}

	@Test
	public void testWrite()
	{
		DataSet<Integer> rs1 = new DataSet<>(a, b, 0);

		for (int i = 0; i < 20; i++) {
			DataRecord<Integer> s = new DataRecord<Integer>(0, 42);
			s.setDateTime(a);
			rs1.addRecord(s);
		}

		assertEquals(this.toBeTested.write(rs1), true);
		File file = new File(this.filePath + a.getTime() + "-" + b.getTime() + ".json");

		assertEquals(file.exists(), true);

		String fileContents = "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}"
				+ "{\"timestamp\":\"Jan 1, 2014 1:00:00 AM\",\"data\":42,\"sensorID\":0,\"meta\":{}}";

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}

			reader.close();
			assertEquals(stringBuilder.toString().equals(fileContents), true);
		}
		catch (IOException e) {
			// failure
			assertEquals(true, false);
		}
	}

	@Test
	public void testRead()
	{
		DataSet<Integer> rs1 = new DataSet<>(a, b, 0);
		DataSet<Integer> rs2 = new DataSet<>(b, e, 0);
		DataSet<Integer> rs3 = new DataSet<>(e, f, 0);

		Calendar cal = new GregorianCalendar();
		cal.setTime(a);
		cal.add(Calendar.MINUTE, 3);

		Date x = cal.getTime();

		cal.setTime(f);
		cal.add(Calendar.MINUTE, -2);

		Date y = cal.getTime();

		DataSet<Integer> readin = new DataSet<>(x, y, 0);
		DataSet<Integer> readinCheck = new DataSet<>(x, y, 0);

		cal.setTime(a);

		for (int i = 0; i < 5; i++) {
			DataRecord<Integer> newRecord = new DataRecord<Integer>(0, 42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs1.addRecord(newRecord);
			if (i > 2) readinCheck.addRecord(newRecord);
		}

		for (int i = 0; i < 5; i++) {
			DataRecord<Integer> newRecord = new DataRecord<Integer>(0, 42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs2.addRecord(newRecord);
			readinCheck.addRecord(newRecord);
		}

		for (int i = 0; i < 5; i++) {
			DataRecord<Integer> newRecord = new DataRecord<Integer>(0, 42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs3.addRecord(newRecord);
			if (i < 4) readinCheck.addRecord(newRecord);
		}

		this.toBeTested.write(rs1);
		this.toBeTested.write(rs2);
		this.toBeTested.write(rs3);

		this.toBeTested.read(readin);

		assertEquals(readin.getRecordCount(), 11);
		assertEquals(readin.equals(readinCheck), true);
	}

	@Test
	public void testDelete()
	{
		DataSet<Integer> rs1 = new DataSet<>(a, b, 0);
		DataSet<Integer> rs2 = new DataSet<>(b, e, 0);
		DataSet<Integer> rs3 = new DataSet<>(e, f, 0);
		DataSet<Integer> rs4 = new DataSet<>(a, f, 0);

		Calendar cal = new GregorianCalendar();
		cal.setTime(a);
		cal.add(Calendar.MINUTE, 3);

		Date x = cal.getTime();

		cal.setTime(f);
		cal.add(Calendar.MINUTE, -2);

		Date y = cal.getTime();

		DataSet<Integer> readin = new DataSet<>(x, y, 0);
		// 1:03, 1:04 1:13
		DataSet<Integer> readinCheck = new DataSet<>(a, f, 0);

		cal.setTime(a);

		for (int i = 0; i < 5; i++) {
			DataRecord<Integer> newRecord = new DataRecord<Integer>(0, 42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs1.addRecord(newRecord);
			if (i < 3) readinCheck.addRecord(newRecord);
		}

		for (int i = 0; i < 5; i++) {
			DataRecord<Integer> newRecord = new DataRecord<Integer>(0, 42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs2.addRecord(newRecord);
		}

		for (int i = 0; i < 5; i++) {
			DataRecord<Integer> newRecord = new DataRecord<Integer>(0, 42);
			newRecord.setDateTime(cal.getTime());
			cal.add(Calendar.MINUTE, 1);
			rs3.addRecord(newRecord);
			if (i > 3) readinCheck.addRecord(newRecord);
		}

		this.toBeTested.write(rs1);
		this.toBeTested.write(rs2);
		this.toBeTested.write(rs3);

		assertEquals(this.toBeTested.delete(readin), true);

		this.toBeTested.read(rs4);

		assertEquals(rs4.getRecordCount(), 4);
		assertEquals(rs4.equals(readinCheck), true);

	}

	@After
	public void tearDown()
	{
		File directory = new File("./Storage/0");
		File[] fileList = directory.listFiles();

		for (File file : fileList) {
			file.delete();
		}
	}
}
