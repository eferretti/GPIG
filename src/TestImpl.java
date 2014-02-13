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
		mgr.addModuleDirectory("~/HUMS_Modules/");
		mgr.refreshModules();
		
		System.out.println(mgr.getAvailableModules());
		
		int id = mgr.createObjectOfModule(mgr.getAvailableModules().get(0).moduleID);

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
	}
}
