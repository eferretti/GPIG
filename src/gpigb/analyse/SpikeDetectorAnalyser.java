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

import java.util.ArrayList;
import java.util.Calendar;
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
	private static final int WINDOW_SIZE = 4;
	private static final int ALPHA = 3;
	
	private StrongReference<Reporter> reporter;
	
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
	public Map<String, ConfigurationValue> getConfigSpec() {
		HashMap<String, ConfigurationValue> configMap = new HashMap<>();
		configMap.put("Reporter", new ConfigurationValue(ValueType.Reporter, 0));
		return configMap;
	}

	@Override
	public boolean setConfig(Map<String, ConfigurationValue> newConfig,
			ComponentManager<Analyser> aMgr,ComponentManager<Reporter> rMgr,
			ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr) {
		try
		{
			this.reporter = rMgr.getObjectByID(newConfig.get("Reporter").intValue);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
		
		Double total = 0.;
		Double mean =  0.;
		Double sd = 0.;
		
		RecordSet<Integer> rs = new RecordSet<Integer>(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), sensorID);
		rs.addRecord(new SensorRecord<Integer>(sensorID, reading));
		List<RecordSet<?>> ls = new ArrayList<>();
		ls.add(rs);
		reporter.get().generateReport(ls);
		
		// if enough data acquired use moving average to smoothen the data and then check if the current reading is an outlier 
		
		
		if(movAvgDiff.size() >= WINDOW_SIZE) {
			    
				if(movAvgDiff.size() > WINDOW_SIZE) movAvgDiff.removeFirst();
			    //Calculate moving average of the last windows in the X (data)
				for(Integer data : rawData) {
					total = total + data;				
				}
				Double m_avg = ((double) total / rawData.size());
				
				total = 0.;
				//Calculate mean in X(t) - Y(t), where Y(t) is the smoother data (the moving average)
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
					System.out.println("SPIKE DETECTED!");
				}
				movAvgDiff.addLast(reading - m_avg);
				if(movAvgDiff.size() > WINDOW_SIZE) movAvgDiff.removeFirst();
			}
		
		// if not enough data acquired use simple method: if current reading is above mean + sd or below mean - sd it's a spike 
		else  if(rawData.size() > 2){ 
			
			//Calculate mean
			for(Integer data : rawData) {
				total = total + data;				
			}
			mean = (double) total / rawData.size();
			movAvgDiff.addLast( reading - mean);
			//Calculate SD
			for(Integer data : rawData) {
				sd = sd + Math.pow((data- mean), 2);
			}
			sd = Math.sqrt(sd / (rawData.size() - 1));
			if(reading >= mean + sd || reading <= mean - sd) {
				System.out.println("SPIKE DETECTED!");
			}
		}
		
		rawData.addLast(reading);
		if(rawData.size() > WINDOW_SIZE) rawData.removeFirst();
		
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
