package gpigb.store;

import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class JSONFileStore implements Store
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
					String filePath = fileLoc + unpopulated.getSensorID() + "/" + fileNames.get(i);
					File file = new File(filePath);
					
					if (file.exists())
					{
						BufferedReader br = new BufferedReader(new FileReader(file));
						
						String line;
						while ((line = br.readLine()) != null)
						{
							Type dataType = new TypeToken<SensorRecord<?>>(){}.getType();
							System.out.println(dataType.toString());
							System.exit(0);
							try
							{
								System.out.println("hello");
								SensorRecord<?> record = (SensorRecord<?>) gson.fromJson(line, dataType);
								System.out.println("goodbye");
								if ((record.getTimestamp().getTime() <= unpopulated.getToTime().getTime() &&
										record.getTimestamp().getTime() >= unpopulated.getFromTime().getTime()))
								{
									unpopulated.addRecord((SensorRecord) record);
								}
							} 
							catch(Throwable t)
							{ 
								t.printStackTrace();
							}
							
						}
					}
					else
					{
						return false;
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
			Type dataType = new TypeToken<SensorRecord<?>>(){}.getType();
			json += gson.toJson(data.getReadingAtPosition(i), dataType) + System.getProperty("line.separator");
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
				
				if ((fFrom.getTimeInMillis() >= from.getTime() && fFrom.getTimeInMillis() <= to.getTime())
						|| (fFrom.getTimeInMillis() <= from.getTime() && fTo.getTimeInMillis() >= to.getTime())
						|| (fTo.getTimeInMillis() >= from.getTime() && fTo.getTimeInMillis() <= to.getTime()))
				{
					
					if (!fileNames.contains(file.getName()))
							fileNames.add(file.getName());
				}
			}
		
			return fileNames;
		}
	}
}
