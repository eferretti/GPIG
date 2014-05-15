package gpigb.sense;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.SensorRecord;

public class LinuxProcessCPUSensor implements Sensor<Float>
{
	float lastReading = 0;
	File statFile = null;
	int pid = 0;
	int cpuID = -1;
	long oldUtime = -1, oldStime = -1;
	List<SensorObserver> observers = new ArrayList<>();
	
	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> spec = new HashMap<>();
		spec.put("Process ID", new ConfigurationValue(ValueType.Integer, 0));
		handler.getConfiguration(spec);
		pid = (Integer)spec.get("Process ID").value;
		statFile = new File("/proc/" + pid + "/stat");
		
		try
		{
			Scanner s = new Scanner(statFile);
			String[] contents = s.nextLine().split("\\s");
			s.close();
			cpuID = Integer.parseInt(contents[38]);
			oldStime = Long.parseLong(contents[14]);
			oldUtime = Long.parseLong(contents[13]);
		}
		catch(Exception e)
		{
			
		}
	}

	private int id;
	@Override
	public int getID()
	{
		return id;
	}

	@Override
	public Float poll()
	{
	    try {
	        RandomAccessFile reader = new RandomAccessFile("/proc/" + pid + "/stat", "r");
	        String load = reader.readLine();

	        String[] toks = load.split(" ");

	        long idle1 = Long.parseLong(toks[4]);
	        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        try {
	            Thread.sleep(360);
	        } catch (Exception e) {}

	        reader.seek(0);
	        load = reader.readLine();
	        reader.close();

	        toks = load.split(" ");

	        long idle2 = Long.parseLong(toks[4]);
	        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
	            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        return 100f*(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    return 0f;
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void registerObserver(SensorObserver obs)
	{
		if(!observers.contains(obs))
			observers.add(obs);
	}

	@Override
	public void removeObserver(SensorObserver obs)
	{
		observers.remove(obs);
	}

	@Override
	public void notifyObservers()
	{
		for(SensorObserver obs : observers)
			obs.update(new SensorRecord<Float>(getID(), lastReading, "CPU_ID", ""+cpuID));
	}

	@Override
	public void setID(int newID)
	{
		this.id = newID;
	}
}
