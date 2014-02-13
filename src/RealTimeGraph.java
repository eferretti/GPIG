import gpigb.analyse.AccelerationAnalyser;
import gpigb.analyse.Analyser;
import gpigb.analyse.ThresholdAnalyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
import gpigb.report.OutOfRangeReporter;
import gpigb.report.Reporter;
import gpigb.report.ReporterPlotRTSmart;
import gpigb.report.UnexpectedAccelerationReporter;
import gpigb.sense.ConcreteSensorTwo;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.InMemoryStore;
import gpigb.store.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RealTimeGraph
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		ConcreteSensorTwo s2 = new ConcreteSensorTwo();

		ComponentManager<Sensor<?>> mgr = new JarFileComponentManager<Sensor<?>>(
				(Class<? extends Sensor<?>>) Sensor.class);
		mgr.addModuleDirectory("./HUMS_Modules/");
		mgr.refreshModules();

		System.out.println(mgr.getAvailableModules());

		int id = mgr.createObjectOfModule(mgr.getAvailableModules().get(0).moduleID);
		// StrongReference<Sensor<?>> ref1 = mgr.getObjectByID(id);
		// Sensor<Float> s1 = (Sensor<Float>) ref1.get();

		StrongReference<Sensor<?>> ref1 = mgr.getObjectByID(id);
		Sensor<Float> s1 = (Sensor<Float>) ref1.get();

		final Store store = new InMemoryStore();

		final Analyser atemp = new ThresholdAnalyser(40, 20);
		final Analyser aelev = new ThresholdAnalyser(1000, 1100);
		final Analyser a2 = new AccelerationAnalyser();

		final Reporter r1 = new OutOfRangeReporter();
		final Reporter r2 = new UnexpectedAccelerationReporter();
		final Reporter r3 = new ReporterPlotRTSmart("Real-time Plot 1");
		final Reporter r4 = new ReporterPlotRTSmart("Real-time Plot 1");

		s2.registerObserver(new SensorObserver<Integer>()
		{
			@Override
			public void update(int sensorID, Integer reading)
			{
				System.out.println("Graphing result");
				DataSet<Integer> rs = new DataSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new DataRecord<Integer>(1, reading));
				store.write(rs);

				List<DataSet<?>> newList = new ArrayList<DataSet<?>>();
				newList.add(rs);
				r3.generateReport(newList);

				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, 1980);
				Date f = c.getTime();
				c.set(Calendar.YEAR, 5000);
				Date t = c.getTime();
				DataSet<Float> tmp = new DataSet<>(f, t, 1);
				store.read(tmp);
				System.out.println("" + tmp.getRecordCount());
			}

			@Override
			public void update(DataRecord<Integer> reading)
			{
				// TODO Auto-generated method stub

			}
		});

		s2.registerObserver(new SensorObserver<Integer>()
		{
			@Override
			public void update(int sensorID, Integer reading)
			{
				DataSet<Integer> rs = new DataSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new DataRecord<Integer>(2, reading));
				store.write(rs);
				List<DataSet<?>> newList = new ArrayList<DataSet<?>>();
				newList.add(rs);
				r4.generateReport(newList);
			}

			@Override
			public void update(DataRecord<Integer> reading)
			{
				// TODO Auto-generated method stub

			}
		});
	}
}
