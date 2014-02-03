package gpigb.analyse;

import gpigb.data.*;
import gpigb.report.Reporter1;

import java.util.*;

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
		if(inputs.getReadingAtPosition(i).getData() < lowerThreshold ||
			inputs.getReadingAtPosition(i).getData() > upperThreshold){
			
			r = true;
			
			List<RecordSet<?>> rt = new ArrayList<RecordSet<?>>();
			RecordSet<Integer> add = new RecordSet<Integer>(inputs.getFromTime(), inputs.getToTime(), inputs.getSensorID());  
			add.addRecord(inputs.getReadingAtPosition(i));
			
			new Reporter1().GenerateReport(rt);
		
		
		}
		
		}
	
	return r;
	
	}
	
public boolean Analyse(List<RecordSet<?>> input){
	return false; 
}

				
	


}