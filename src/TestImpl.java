import gpigb.classloading.ComponentManager;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.report.SimpleWebReporter;
import gpigb.sense.SNMPSensor;
import gpigb.sense.Sensor;

import java.io.IOException;

public class TestImpl
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		//ConcreteSensorTwo s2 = new ConcreteSensorTwo();
		
		ComponentManager<Sensor<?>> mgr = new JarFileComponentManager<Sensor<?>>((Class<? extends Sensor<?>>) Sensor.class);
		mgr.addModuleDirectory("./HUMS_Modules/");
		mgr.refreshModules();
		
		System.out.println(mgr.getAvailableModules());
		
		int id = mgr.createObjectOfModule(mgr.getAvailableModules().get(0).moduleID);
		//StrongReference<Sensor<?>> ref1 = mgr.getObjectByID(id);
		//Sensor<Float> s1 = (Sensor<Float>) ref1.get();

		StrongReference<Sensor<?>> ref1 = mgr.getObjectByID(id);
		Sensor<Float> s1 = new SNMPSensor();
		
		SimpleWebReporter r1 = new SimpleWebReporter(8080);
		
		s1.registerObserver(r1);
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		final Store store = new MongoStore();
		
		final Analyser atemp = new ThresholdAnalyser(40, 20);
		final Analyser aelev = new ThresholdAnalyser(1000, 1100);
		final Analyser a2 = new Analyser2();
		
		final Reporter r1 = new Reporter1();
		final Reporter r2 = new Reporter2();
		*/
//		final Reporter r3 = new ReporterPlotRTSmart("Real-time Plot 1");
//		
//		s1.registerObserver(new SensorObserver<Float>()
//		{
//			@Override
//			public void update(int sensorID, Float reading)
//			{
//				System.out.println("Graphing result");
//				RecordSet<Integer> rs = new RecordSet<>(new Date(), new Date(), sensorID);
//				rs.addRecord(new SensorRecord<Integer>(1, reading.intValue()));
//				List<RecordSet<?>> newList = new ArrayList<RecordSet<?>>();
//				newList.add(rs);
//				r3.generateReport(newList);
////				store.write(rs);
//				/*
//				Calendar c = Calendar.getInstance();
//				c.set(Calendar.YEAR, 1980);
//				Date f = c.getTime();
//				c.set(Calendar.YEAR, 5000);
//				Date t = c.getTime();
//				RecordSet<Float> tmp = new RecordSet<>(f, t, 1);
//				store.read(tmp);
//				System.out.println("" + tmp.getRecordCount());
//				*/
//			}
//		});
		
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
