package gpigb.analyse;

import java.util.ArrayList;
import java.util.List;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter2;
import gpigb.report.ReporterPlot;

public class Analyser2 implements Analyser{
	
	/**
	 * 
	 * @param data
	 */
	public boolean Analyse(List<RecordSet<?>> data)
	{
		List<RecordSet<?>> changeRecord = new ArrayList <RecordSet<?>>();
		/*
		for(int i = 0; i <data.size(); i++)
		{
			//Check that there are at least 2 records
			if(data.get(i).getRecordCount() < 2)
				return false;
			SensorRecord<?> x = data.get(i).getReadingAtPosition(0);
			SensorRecord<?> y = data.get(i).getReadingAtPosition(1);
			// Check for sharp increase or decrease (Just simple > at the moment)
			if ((Integer) x.getData() > (Integer) y.getData())
			{
				changeRecord.add(data.get(i));	
			}
		}
		
		// Send any data with sharp increase to reporter
		if (!changeRecord.isEmpty())
			new Reporter2().GenerateReport(changeRecord);
		*/
		// Calculate average of every RecordSet
		List<RecordSet<?>> averageRecord = new ArrayList <RecordSet<?>>();
		Integer previousReading = 0;
		Integer difference = 300;
		for(int j = 0; j < data.size(); j++)
		{
			if(data.get(j).getRecordCount() == 0)
				return false;
			Integer average = (Integer) data.get(j).getReadingAtPosition(0).getData();
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
		
		// Send to reporter
		if (!changeRecord.isEmpty())
			new Reporter2().GenerateReport(changeRecord);
		//if (!averageRecord.isEmpty())
		//	new ReporterPlot().GenerateReport(averageRecord);
		
		
		//Reporter3.GenerateReport(averageRecord);
		
		return true;
	}
	
	public boolean Analyse(RecordSet<?> input){
		return false;
	}
	

}
