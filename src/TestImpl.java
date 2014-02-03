import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gpigb.analyse.Analyser;
import gpigb.analyse.Analyser2;
import gpigb.analyse.ThresholdAnalyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.classloading.JarFileComponentManager;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.report.Reporter1;
import gpigb.report.Reporter2;
import gpigb.sense.ConcreteSensorOne;
import gpigb.sense.ConcreteSensorTwo;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.InMemoryStore;
import gpigb.store.Store;

public class TestImpl
{
	public static void main(String[] args)
	{
		ConcreteSensorOne s1 = new ConcreteSensorOne();
		//Sensor<Integer> s2 = new ConcreteSensorTwo();
		
		final Store store = new InMemoryStore();
		
		final Analyser atemp = new ThresholdAnalyser(40, 20);
		final Analyser aelev = new ThresholdAnalyser(1000, 10100);
		final Analyser a2 = new Analyser2();
		
		final Reporter r1 = new Reporter1();
		final Reporter r2 = new Reporter2();
		
		s1.registerObserver(new SensorObserver<Integer>()
		{
			@Override
			public void update(int sensorID, Integer reading)
			{
				RecordSet<Integer> rs = new RecordSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new SensorRecord<Integer>(1, reading));
				store.write(rs);
			}
		});
		
//		s2.registerObserver(new SensorObserver<Integer>()
//				{
//					@Override
//					public void update(int sensorID, Integer reading)
//					{
//						RecordSet<Integer> rs = new RecordSet<>(new Date(), new Date(), sensorID);
//						rs.addRecord(new SensorRecord<Integer>(2, reading));
//						store.write(rs);
//					}
//				});
//	
		(new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				while(true)
				{
					System.out.println("Looping...");
					try{Thread.sleep(1000);}catch(Exception e){}
					
					Calendar c = Calendar.getInstance();
					c.set(2010, 1, 1, 1, 1);
					Date d1 = c.getTime();
					c.set(2015, 1, 1, 1, 1);
					Date d2 = c.getTime();
					RecordSet<Integer> rs = new RecordSet<Integer>(d1, d2, 1);
					store.read(rs);
					System.out.println("\tResult: " + (atemp.Analyse(rs) ? "True" : "False"));
				}
			}
		})).start();
	}

}