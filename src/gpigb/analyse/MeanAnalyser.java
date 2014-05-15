package gpigb.analyse;

import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;
import gpigb.analyse.Analyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeanAnalyser implements Analyser {
	private int id;
	private Integer range;
	public StrongReference<Store> store;
	
	public MeanAnalyser()
	{
		
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
		RecordSet<Double> rawDataRecord = new RecordSet<Double>(input.getFromTime(), input.getToTime(), input.getSensorID());
		
		
		Double total = 0.0;
		Double mean =  0.0;
		if (!this.populateRecordSet(rawDataRecord))
		{
			return false;
		}else{
			for(int i = 0 ; i < rawDataRecord.getRecordCount(); i++)
			{
				 total = total + (Double)rawDataRecord.getDataAtPosition(i).getData();				
			}
			mean = total /rawDataRecord.getRecordCount();
			// Create new record and add to list
			SensorRecord<Double> as = new SensorRecord<Double>(input.getSensorID(), mean);
			((RecordSet<Double>)input).addRecord(as);
			return true;
		}
		
	}
	
	public boolean populateRecordSet(RecordSet<?> input){
		if(store == null || store.get() == null)
			return false;
		
		store.get().read(input);
		return true;
	}

}
