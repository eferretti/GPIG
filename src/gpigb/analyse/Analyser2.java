package gpigb.analyse;

import java.util.ArrayList;
import java.util.List;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter2;

public class Analyser2 implements Analyser{
	
	/**
	 * 
	 * @param data
	 */
	public boolean Analyse(List<RecordSet<?>> data)
	{
		
		
		List<RecordSet<?>> changeRecord = new ArrayList <RecordSet<?>>();
		for(int i = 0; i <data.size(); i++)
		{
			SensorRecord<?> x = data.get(i).getReadingAtPosition(0);
			SensorRecord<?> y = data.get(i).getReadingAtPosition(1);
			// Check for sharp increase or decrease
			if ((Integer) x.getData() > (Integer) y.getData())
			{
				changeRecord.add(data.get(i));	
			}
		}
		
		// Send any data with sharp increase to reporter
		new Reporter2().GenerateReport(changeRecord);
		
		// Calculate average of every RecordSet
		List<RecordSet<?>> averageRecord = new ArrayList <RecordSet<?>>();
		for(int j = 0; j < data.size(); j++)
		{
			Double average = (Double) data.get(j).getReadingAtPosition(0).getData();
			for(int i = 1; i < data.get(j).getRecordCount(); i++)
			{
				average = average + (Integer) data.get(j).getReadingAtPosition(i).getData();
			}
			// Calculate average
			average = average / data.get(j).getRecordCount();
			// Create new record and add to list
			RecordSet<Double> av = new RecordSet<Double>(data.get(j).getFromTime(), data.get(j).getToTime(), data.get(j).getSensorID());
			SensorRecord<Double> as = new SensorRecord<Double>(data.get(j).getSensorID(), average, null);
			av.addRecord(as);
			averageRecord.add(av);
		}
		// Send to reporter
		//Reporter3.GenerateReport(averageRecord);
		return true;
	}
	
	public boolean Analyse(RecordSet<?> input){
		return false;
	}
	

}
