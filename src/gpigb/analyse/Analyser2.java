package gpigb.analyse;

import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter2;

import java.util.ArrayList;
import java.util.List;

/**
 * An analyser which reports significant acceleration in a sensor reading 
 */
public class Analyser2 implements Analyser
{
	/**
	 * Perform analysis on sensor reading histories. If any history has a value
	 * which changes by more than a given threshold then report only the offending
	 * histories.
	 * @param data The set of sensor histories to analyse
	 */
	public boolean Analyse(List<RecordSet<?>> data)
	{
		List<RecordSet<?>> changeRecord = new ArrayList <RecordSet<?>>();
		List<RecordSet<?>> averageRecord = new ArrayList <RecordSet<?>>();
		
		Integer previousReading = 0;
		Integer difference = 300;
		for(int j = 0; j < data.size(); j++)
		{
			// No data has been provided
			if(data.get(j).getRecordCount() == 0)
				return false;
			
			Integer average = (Integer) data.get(j).getReadingAtPosition(0).getData();
			previousReading = average;
			for(int i = 1; i < data.get(j).getRecordCount(); i++)
			{
				Integer currentReading = (Integer) data.get(j).getReadingAtPosition(i).getData();
				average = average + currentReading;
				
				// If there is a difference between readings add the RecordSet where the change occurred
				if (currentReading > previousReading + difference || currentReading  < previousReading - difference)
					changeRecord.add(data.get(j));
				
				previousReading = (Integer) data.get(j).getReadingAtPosition(i).getData();
			}
			
			// Calculate average
			average = average / data.get(j).getRecordCount();
			
			// Create new record and add to list
			RecordSet<Integer> av = new RecordSet<Integer>(data.get(j).getFromTime(), data.get(j).getToTime(), data.get(j).getSensorID());
			SensorRecord<Integer> as = new SensorRecord<Integer>(data.get(j).getSensorID(), average);
			av.addRecord(as);
			averageRecord.add(av);
		}
		
		return true;
	}
	
	/**
	 * Invokes {@link gpigb.analyse.Analyser2#Analyse(List)} with a list containing the argument
	 * @see gpigb.analyse.Analyser#Analyse(RecordSet)
	 */
	public boolean Analyse(RecordSet<?> input)
	{
		ArrayList<RecordSet<?>> a = new ArrayList<>();
		a.add(input);
		return Analyse(a);
	}
	

}
