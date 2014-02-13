import gpigb.analyse.RealTimeGraphAnalyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.ReporterPlotRTSmart;
import gpigb.report.SimpleWebReporter;
import gpigb.sense.ConcreteSensorOne;
import gpigb.sense.SNMPSensor;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestImpl
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		ComponentManager<Sensor<?>> mgr = new JarFileComponentManager<Sensor<?>>((Class<? extends Sensor<?>>) Sensor.class);
		mgr.addModuleDirectory("./HUMS_Modules/");
		mgr.refreshModules();
		
		System.out.println(mgr.getAvailableModules());
		
		int id = mgr.createObjectOfModule(mgr.getAvailableModules().get(0).moduleID);

		StrongReference<Sensor<?>> ref1 = mgr.getObjectByID(id);
		Sensor<Float> s1 = new SNMPSensor();
		
		SimpleWebReporter r1 = new SimpleWebReporter(8080);
		
		s1.registerObserver(r1);
		
		final ReporterPlotRTSmart r2 = new ReporterPlotRTSmart("Title");
		
		ConcreteSensorOne sen2 = new ConcreteSensorOne();
		
		s1.registerObserver(new RealTimeGraphAnalyser());
	}
}
