package gpigb.analyse;

import gpigb.data.RecordSet;
import gpigb.store.Store;

import java.util.List;

public class AnalyserGUI implements Analyser{
	
	public Store store;
	
	@Override
	public boolean Analyse(List<RecordSet<?>> input) {
		return false;
	}

	@Override
	public boolean Analyse(RecordSet<?> input) {
		store.read(input);
		
		//SensorRecord<Integer> data = new SensorRecord<Integer>(1,100);
		//SensorRecord<Integer> data2 = new SensorRecord<Integer>(1,50);
		//input.addRecord((SensorRecord) data);
		//input.addRecord((SensorRecord) data2); 
		return false;
	}
	

}
