package gpigb.testruns;

import java.util.Calendar;
import java.util.Map;

import gpigb.classloading.IDGenerator;
import gpigb.classloading.JarFileComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.scheduler.Scheduler;
import gpigb.sense.Sensor;

public class SchedulerTest
{
	public static void main(String[] args)
	{
		IDGenerator.setMinID(47);
		
		JarFileComponentManager<Sensor> seMgr = new JarFileComponentManager<>(Sensor.class);
		
		seMgr.addModuleDirectory("~/HUMS_Modules");
		
		seMgr.refreshModules();
		
		int id = seMgr.getModuleIDByName("gpigb.sense.LinuxProcessCPUSensor");
		id = seMgr.createObjectOfModule(id);
		StrongReference<Sensor> sensor = seMgr.getObjectByID(id);
		
		Map<String, ConfigurationValue> config = sensor.get().getConfigSpec();
		config.get("Process ID").intValue = 21794;
		sensor.get().setConfig(config, null, null, null, null);
		
		Scheduler sched = new Scheduler();
		System.out.println("Sensor starting in 3 seconds");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 3);
		
		sched.addSensorTask(cal.getTime(), 1000, sensor);
		
		while(true)
			try{Thread.sleep(100);}catch(Exception e){}
	}
}
