package gpigb.sense;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.store.Store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinuxProcessCPUSensor implements Sensor<Float>
{
	float lastReading = 0;
	File statFile = null;
	int pid = 21794;
	int cpuID = -1;
	long oldUtime = -1, oldStime = -1;
	List<SensorObserver> observers = new ArrayList<>();
	Float averageUsage = null;
	Float dampingFactor = 0.5f;
	
	public static void main(String[] args)
	{
		LinuxProcessCPUSensor c = new LinuxProcessCPUSensor();
		while(true)
			try{Thread.sleep(100);}catch(Exception e){}
	}
	
	public LinuxProcessCPUSensor()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(true)
				{
					try
					{
						Thread.sleep(500);
					}catch(Exception e){}
					System.out.println("Reading: " + poll());
				}
			}
		}).start();
	}
	
	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> spec = new HashMap<>();
		spec.put("Process ID", new ConfigurationValue(ValueType.Integer, pid));
		return spec;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			pid = (Integer)newSpec.get("Process ID").intValue;
			statFile = new File("/proc/" + pid + "/stat");
			return true;
		}
		catch(Exception e)
		{
			return false;
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

	        long sutime = Long.parseLong(toks[13]);
	        long sstime = Long.parseLong(toks[14]);
	        long scutime = Long.parseLong(toks[15]);
	        long scstime = Long.parseLong(toks[16]);
	        
	        String core = toks[38].trim();

	        reader.close();
	        reader = new RandomAccessFile("/proc/stat", "r");
	        toks = reader.readLine().split(" ");
	        reader.close();
	        long time1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[5])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
	        
	        try
	        {
	        	Thread.sleep(100);
	        }
	        catch(Exception e)
	        {
	        	
	        }
	        
	        reader.close();
	        reader = new RandomAccessFile("/proc/" + pid + "/stat", "r");
	        load = reader.readLine();
	        reader.close();
	        toks = load.split(" ");

	        long eutime = Long.parseLong(toks[13]);
	        long estime = Long.parseLong(toks[14]);
	        long ecutime = Long.parseLong(toks[15]);
	        long ecstime = Long.parseLong(toks[16]);
	        reader.close();
	        reader = new RandomAccessFile("/proc/stat", "r");
	        toks = reader.readLine().split(" ");
	        reader.close();
	        long time2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[5])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
	        
	        long dutime = eutime - sutime;
	        long dstime = estime - sstime;
	        long dcutime = ecutime - scutime;
	        long dcstime = ecstime - scstime;
	        long dt = time2 - time1;
	        
	        Float instantaniousUsage = 100*((dutime + dstime + dcutime + dcstime)/(1.0f*dt));

	        if(averageUsage == null)
	        {
	        	averageUsage = instantaniousUsage;
	        }
	        else
	        {
	        	averageUsage = (dampingFactor * averageUsage) + ((1-dampingFactor) * instantaniousUsage);
	        }
	        
	        return averageUsage;
	        
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
	
	@Override
	public int getConfigurationStepNumber() {
		
		return 1;
	}
}
