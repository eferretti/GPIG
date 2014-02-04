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
import gpigb.report.*;
import gpigb.sense.ConcreteSensorOne;
import gpigb.sense.ConcreteSensorTwo;
import gpigb.sense.SNMPSensor;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.FileStore;
import gpigb.store.JSONFileStore;
import gpigb.store.Store;

public class TestImpl
{
	public static void main(String[] args)
	{
		//ConcreteSensorOne s1 = new ConcreteSensorOne();
//		ConcreteSensorTwo s2 = new ConcreteSensorTwo();
		
		ComponentManager<Sensor<?>> mgr = new JarFileComponentManager<Sensor<?>>((Class<? extends Sensor<?>>) Sensor.class);
		mgr.addModuleDirectory("./HUMS_Modules/");
		mgr.refreshModules();
		
		System.out.println(mgr.getAvailableModules());
		
		int id = mgr.createObjectOfModule(mgr.getAvailableModules().get(0).moduleID);
		StrongReference<Sensor<?>> ref1 = mgr.getObjectByID(id);
		Sensor<Float> s1 = (Sensor<Float>) ref1.get();
		
		final Store store = new JSONFileStore();
		
		final Analyser atemp = new ThresholdAnalyser(40, 20);
		final Analyser aelev = new ThresholdAnalyser(1000, 1100);
		final Analyser a2 = new Analyser2();
		
		final Reporter r1 = new Reporter1();
		final Reporter r2 = new Reporter2();
		final Reporter r3 = new ReporterPlotRT("Real-time Plot 1");
		//final Reporter r4 = new ReporterPlot();
		
		
		s1.registerObserver(new SensorObserver<Float>()
		{
			@Override
			public void update(int sensorID, Float reading)
			{
				System.out.println("Graphing result");
				RecordSet<Float> rs = new RecordSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new SensorRecord<Float>(1, reading));
				List<RecordSet<?>> newList = new ArrayList<RecordSet<?>>();
				newList.add(rs);
				r3.GenerateReport(newList);
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
	}
}
