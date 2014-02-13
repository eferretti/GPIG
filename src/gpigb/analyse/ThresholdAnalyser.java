package gpigb.analyse;

import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter1;

import java.util.ArrayList;
import java.util.List;

public class ThresholdAnalyser implements Analyser{
	
	int upperThreshold;
	int lowerThreshold;

	public ThresholdAnalyser(int u, int l){
	
		upperThreshold = u;
		lowerThreshold = l;
		
	}
	

public boolean Analyse(RecordSet<?> input){
	RecordSet<Integer> inputs = (RecordSet<Integer>)input;
	int size = inputs.getRecordCount();
	boolean r = false;
	
	for(int i = 0; i<size; i++){
		SensorRecord<Integer> rec = inputs.getReadingAtPosition(i);
		if(rec.getData() < lowerThreshold ||
			rec.getData() > upperThreshold){
			
			r = true;
			
			List<RecordSet<?>> rt = new ArrayList<RecordSet<?>>();
			RecordSet<Integer> add = new RecordSet<Integer>(inputs.getFromTime(), inputs.getToTime(), inputs.getSensorID());  
			add.addRecord(inputs.getReadingAtPosition(i));
			rt.add(add);
			
			new Reporter1().generateReport(rt);
		
		
		}
		
		}
	
	return r;
	
	}
	
public boolean Analyse(List<RecordSet<?>> input){
	return false; 
}

				
	


}