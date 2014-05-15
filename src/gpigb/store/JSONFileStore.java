package gpigb.store;

import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.SensorRecord;
import gpigb.data.RecordSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.util.Hash;

/**
 * A store implementation which stores objects as JSON files on the local file
 * system.
 */
public class JSONFileStore implements Store
{
	private String fileLoc = "./Storage/";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean read(RecordSet<?> unpopulated)
	{
		ArrayList<String> fileNames = findFiles(unpopulated.getToTime(), unpopulated.getFromTime(),
				unpopulated.getSensorID());
		Gson gson = new Gson();

		if (fileNames != null && fileNames.size() > 0) {
			for (int i = 0; i < fileNames.size(); i++) {
				try {
					String filePath = fileLoc + unpopulated.getSensorID() + "/" + fileNames.get(i);
					File file = new File(filePath);

					if (file.exists()) {
						BufferedReader br = new BufferedReader(new FileReader(file));

						String line;
						while ((line = br.readLine()) != null) {
							Type dataType = new TypeToken<SensorRecord<?>>()
							{
							}.getType();
							try {
								SensorRecord<?> record = gson.fromJson(line, dataType);

								long from = unpopulated.getFromTime().getTime() - 1000;

								if ((record.getTimestamp().getTime() <= unpopulated.getToTime().getTime() && record
										.getTimestamp().getTime() >= from)) {
									unpopulated.addRecord((SensorRecord) record);
								}
							}
							catch (Throwable t) {
								t.printStackTrace();
							}
						}

						br.close();
					}
					else {
						return false;
					}
				}
				catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean write(RecordSet<?> data)
	{
		Gson gson = new Gson();
		String json = "";

		for (int i = 0; i < data.getRecordCount(); i++) {
			Type dataType = new TypeToken<SensorRecord<?>>()
			{
			}.getType();
			json += gson.toJson(data.getDataAtPosition(i), dataType) + System.getProperty("line.separator");
		}

		try {
			String filePath = fileLoc + data.getSensorID() + "/" + data.getFromTime().getTime() + "-"
					+ data.getToTime().getTime() + ".json";
			File f = new File(filePath);
			f.getParentFile().mkdirs();
			FileWriter file = new FileWriter(f);
			file.write(json);
			file.flush();
			file.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean delete(RecordSet<?> items)
	{
		ArrayList<String> fileNames = findFiles(items.getToTime(), items.getFromTime(), items.getSensorID());
		Gson gson = new Gson();

		if (fileNames != null && fileNames.size() > 0) {
			for (int i = 0; i < fileNames.size(); i++) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(fileLoc + items.getSensorID() + "/"
							+ fileNames.get(i)));

					ArrayList<SensorRecord<?>> records = new ArrayList<>();
					Type dataType = new TypeToken<SensorRecord<?>>()
					{
					}.getType();
					for (String x = br.readLine(); x != null; x = br.readLine()) {
						SensorRecord<?> record = gson.fromJson(x, dataType);

						if (record.getTimestamp().getTime() <= items.getToTime().getTime()
								&& record.getTimestamp().getTime() >= items.getFromTime().getTime()) {
							// "delete"
							// basically just dont write back to file
						}
						else {
							records.add(record);
						}
					}

					br.close();

					if (records.size() > 0) {
						File file = new File(fileLoc + items.getSensorID() + "/" + fileNames.get(i));
						if (file.delete()) {
							Collections.sort(records);
							RecordSet<?> rs = new RecordSet<>(records.get(0).getTimestamp(), records
									.get(records.size() - 1).getTimestamp(), items.getSensorID());

							for (SensorRecord record : records) {
								rs.addRecord(record);
							}

							if (this.write(rs) == false) return false;
						}
						else {
							System.out.println("Couldnt remove file");
							return false;
						}
					}
					else {
						File file = new File(fileLoc + items.getSensorID() + "/" + fileNames.get(i));
						file.delete();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			return true;
		}
		else {
			return false;
		}
	}

	private ArrayList<String> findFiles(Date to, Date from, int sensor)
	{
		ArrayList<String> fileNames = new ArrayList<String>();

		File directory = new File(fileLoc + sensor + "/");
		File[] fileList = directory.listFiles();

		if (fileList == null || fileList.length < 0) {
			return null;
		}
		else {
			for (File file : fileList) {
				String[] times = file.getName().split("\\.")[0].split("-");
				if (times.length < 2) continue;
				Calendar fTo = Calendar.getInstance();
				Calendar fFrom = Calendar.getInstance();
				fTo.setTimeInMillis(Long.parseLong(times[0]));
				fFrom.setTimeInMillis(Long.parseLong(times[1]));

				if ((fFrom.getTimeInMillis() >= from.getTime() && fFrom.getTimeInMillis() <= to.getTime())
						|| (fFrom.getTimeInMillis() <= from.getTime() && fTo.getTimeInMillis() >= to.getTime())
						|| (fTo.getTimeInMillis() >= from.getTime() && fTo.getTimeInMillis() <= to.getTime())) {

					if (!fileNames.contains(file.getName())) fileNames.add(file.getName());
				}
			}
			return fileNames;
		}
	}

	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("StorePath", new ConfigurationValue(ValueType.String, fileLoc));
		
		handler.getConfiguration(configSpec);
		
		this.fileLoc = (String) configSpec.get("StorePath").value;
	}

	private int id;
	public void setID(int newID)
	{
		this.id = newID;
	}
	
	public int getID()
	{
		return this.id;
	}

	@Override
	public boolean update(int sensorID, Integer reading) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(int sensorID, Double reading) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(int sensorID, String reading) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(SensorRecord<?> reading) {
		// TODO Auto-generated method stub
		return false;
	}
}
