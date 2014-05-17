package gpigb.analyse;

import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class SpikeDetectorAnalyser implements RealTimeAnalyser{
	private int id;
	private LinkedList<Double> movAvgDiff;
	private LinkedList<Integer> rawData;
	public StrongReference<Store> store;
	private static final int WINDOW_SIZE = 5;
	private static final int ALPHA = 4;
	
	public SpikeDetectorAnalyser() {
		movAvgDiff = new LinkedList<>();
		rawData = new LinkedList<>();
	}
	
	@Override
	public void setID(int newID) {
		id = newID;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public HashMap<String, ConfigurationValue> getConfigSpec() 
	{
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Store", new ConfigurationValue(ValueType.Store, 0));
		return configMap;
	}
	
	public boolean setConfig(Map<String, ConfigurationValue> newConfig, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.store = stMgr.getObjectByID(newConfig.get("Store").intValue);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean analyse(List<RecordSet<?>> input) {
		return false;
	}
	

	@Override
	public boolean analyse(RecordSet<?> input) {
		return false;
	}
	

	@Override
	public boolean update(int sensorID, Integer reading) {
		
		/* if enough data acquired use moving average to smoothen the data and then check if the current reading is an outlier */
		if(rawData.size() >= WINDOW_SIZE) {
			    rawData.addLast(reading);
				ListIterator<Integer> itRaw = rawData.listIterator(rawData.size() - WINDOW_SIZE);
				Integer sum = 0;
				while(itRaw.hasNext()) {
					sum += itRaw.next();
				}
				Double m_avg = reading - ((double) sum / WINDOW_SIZE);
				movAvgDiff.addLast( reading - m_avg);
				Double total = 0.;
				Double mean =  0.;
				Double sd = 0.;
	
	
				//Calculate mean
				for(Double dataDiff : movAvgDiff) {
					total = total + dataDiff;				
				}
				mean = total / movAvgDiff.size();
				
				//Calculate SD
				for(Double dataDiff : movAvgDiff) {
					sd = sd + Math.pow((dataDiff- mean), 2);
				}
				sd = Math.sqrt(sd / (movAvgDiff.size() - 1));
				if(Math.abs(reading - m_avg) > ALPHA * sd) {
					/* need to change to some Reporter .. which also means that we need to have config for that reporter (not only store) */
					System.out.println("SPIKE DETECTED!");
				}
			}
		/* if not enough data acquired use simple method: if current reading is above mean + sd or below mean - sd it's a spike */
		else if (rawData.size() > 2) { 
			Double total = 0.;
			Double mean =  0.;
			Double sd = 0.;
			
			//Calculate mean
			for(Double dataDiff : movAvgDiff) {
				total = total + dataDiff;				
			}
			mean = total / movAvgDiff.size();
			
			//Calculate SD
			for(Double dataDiff : movAvgDiff) {
				sd = sd + Math.pow((dataDiff- mean), 2);
			}
			sd = Math.sqrt(sd / (movAvgDiff.size() - 1));
			
			if(reading >= mean + sd || reading <= mean - sd) {
				/* need to change to some Reporter .. which also means that we need to have config for that reporter (not only store) */
				System.out.println("SPIKE DETECTED!");
			}
			rawData.add(reading);	
		}
		
		
		return false;
	}
	
	public Integer sumList(LinkedList<Integer> list)
	{
		Integer sum = 0;
		for (Integer i : list)
			sum += i;
		return sum;
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

	@Override
	public int getConfigurationStepNumber() {
		// TODO Auto-generated method stub
		return 1;
	}
}
