package gpigb.store;

import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import com.google.gson.Gson;

public class FileStore implements Store
{
	private final static String fileLoc = "./Storage/";
	
	@Override
	public boolean read(RecordSet<?> unpopulated) 
	{
		ArrayList<String> fileNames = findFiles(unpopulated.getToTime(), unpopulated.getFromTime(), unpopulated.getSensorID());
		Gson gson = new Gson();
		
		if (fileNames != null && fileNames.size() > 0)
		{
			for (int i = 0; i < fileNames.size(); i++)
			{
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(fileLoc + unpopulated.getSensorID() + "/" + fileNames.get(i)));
					
					ArrayList<SensorRecord<?>> records = new ArrayList<>();
					for (String x = br.readLine(); x != null; x = br.readLine())
					{
						SensorRecord<?> record = (SensorRecord<?>) gson.fromJson(x, SensorRecord.class);
						
						if ((record.getTimestamp().before(unpopulated.getToTime()) && record.getTimestamp().after(unpopulated.getFromTime()))
								|| record.getTimestamp().equals(unpopulated.getToTime())
								|| record.getTimestamp().equals(unpopulated.getFromTime()))
						{
							unpopulated.addRecord((SensorRecord) record);
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return false;
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean write(RecordSet<?> data) 
	{
		Gson gson = new Gson();
		String json = "";
		
		for (int i = 0; i < data.getRecordCount(); i++)
		{
			json += gson.toJson(data.getReadingAtPosition(i));
		}
		
		try
		{
			String filePath = fileLoc + data.getSensorID() + "/" + data.getFromTime().getTime() 
						+ "-" + data.getToTime().getTime() + ".json";
			File f = new File(filePath);
			f.getParentFile().mkdirs();
			FileWriter file = new FileWriter(f);
			file.write(json);
			file.flush();
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean delete(RecordSet<?> items) 
	{
		ArrayList<String> fileNames = findFiles(items.getToTime(), items.getFromTime(), items.getSensorID());
		Gson gson = new Gson();
		
		if (fileNames != null && fileNames.size() > 0)
		{
			for (int i = 0; i < fileNames.size(); i++)
			{
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(fileLoc + items.getSensorID() + "/" + fileNames.get(i)));
					
					ArrayList<SensorRecord<?>> records = new ArrayList<>();
					for (String x = br.readLine(); x != null; x = br.readLine())
					{
						SensorRecord<?> record = gson.fromJson(x, SensorRecord.class);
						
						if (record.getTimestamp().before(items.getToTime()) && record.getTimestamp().after(items.getFromTime()))
						{
							// "delete"
						}
						else
						{
							records.add(record);
						}
					}
					
					if (records.size() > 0)
					{
						File file = new File(fileLoc + items.getSensorID() + "/" + fileNames.get(i));
						if (file.delete())
						{
							Collections.sort(records);
							RecordSet<SensorRecord<?>> rs = new RecordSet<SensorRecord<?>>(records.get(0).getTimestamp(),
									records.get(records.size()).getTimestamp(), items.getSensorID());
							this.write(rs);
						}
						else
						{
							return false;
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return false;
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private ArrayList<String> findFiles(Date to, Date from, int sensor)
	{
		ArrayList<String> fileNames = new ArrayList<String>();
		
		File directory = new File(fileLoc + sensor + "/");
		File[] fileList = directory.listFiles();
		
		if (fileList == null || fileList.length < 0)
		{
			return null;
		}
		else
		{
			for (File file : fileList)
			{
				String[] times = file.getName().split("\\.")[0].split("-");
				Calendar fTo = Calendar.getInstance();
				Calendar fFrom = Calendar.getInstance();
				fTo.setTimeInMillis(Long.parseLong(times[0]));
				fFrom.setTimeInMillis(Long.parseLong(times[1]));
				
				if ((fFrom.after(from) && fTo.before(to))
						|| (fFrom.after(from) && fFrom.before(to))
						|| (fTo.after(from) && fTo.before(to)))
				{
					if (!fileNames.contains(file.getName()))
							fileNames.add(file.getName());
				}
			}
		
			return fileNames;
		}
	}
}
