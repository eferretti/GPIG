package gpigb.sense;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.Patchable;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.store.Store;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
/**
 * 
 * Sensor driver which reads JVM usage via an SNMP interface
 *
 */
public class SNMPSensor extends Patchable implements Sensor<Float>, Runnable
{
	private ArrayList<WeakReference<SensorObserver<Float>>> observers = new ArrayList<>();
	private Float lastReading = null;

	// JVM Heap Used OID
	private String oidString = "1.3.6.1.4.1.42.2.145.3.163.1.1.2.11.0";

	// Default SNMP settings
	private String port = "16500";

	public SNMPSensor()
	{
		new Thread(this).start();
	}

	public SNMPSensor(Object oldInstance)
	{
		super(oldInstance);
	}

	@Override
	public int getID()
	{
		return 0;
	}

	@Override
	public Float poll()
	{
		return lastReading;
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void registerObserver(SensorObserver<Float> obs)
	{
		for (WeakReference<SensorObserver<Float>> ref : observers) {
			if (ref.get() == obs) { return; }
		}

		observers.add(new WeakReference<SensorObserver<Float>>(obs));
	}

	@Override
	public void removeObserver(SensorObserver<Float> obs)
	{
		for (WeakReference<SensorObserver<Float>> ref : observers) {
			if (ref.get() == obs) {
				observers.remove(ref);
			}
		}
	}

	@Override
	public void notifyObservers()
	{
		ArrayList<WeakReference<SensorObserver<Float>>> toRemove = new ArrayList<>();

		for (WeakReference<SensorObserver<Float>> ref : observers) {
			if (ref.get() == null) {
				toRemove.add(ref);
				continue;
			}

			ref.get().update(new SensorRecord<Float>(getID(), lastReading, "Unit", "MB"));
		}
	}

	@Override
	public void run()
	{
		while (true) {
			try {
				Snmp snmp = null;

				PDU req = new PDU();
				req.setType(PDU.GET);

				CommunityTarget target = new CommunityTarget();
				synchronized(this)
				{
					OID oid = new OID(oidString);
					req.add(new VariableBinding(oid));
					Address a = new UdpAddress("localhost/" + port);
					target.setAddress(a);
				}
				target.setTimeout(50);
				target.setRetries(3);
				target.setCommunity(new OctetString("public"));
				target.setVersion(SnmpConstants.version2c);

				try {
					snmp = new Snmp(new DefaultUdpTransportMapping());
					snmp.listen();
				}
				catch (Exception e) {
				}

				ResponseEvent responseEvent = snmp.send(req, target);

				PDU responsePDU = responseEvent.getResponse();
				VariableBinding binding = responsePDU.getVariableBindings().get(0);
				lastReading = Float.valueOf(binding.getVariable().toString());
				lastReading /= (1000 * 1000); // Convert to MB

				notifyObservers();

				Thread.sleep(50);
			}
			catch (Exception e) {
			}
		}
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> spec = new HashMap<>();
		spec.put("OID", new ConfigurationValue(ValueType.String, oidString));
		spec.put("Port", new ConfigurationValue(ValueType.String, port));
		return spec;
//		handler.getConfiguration(spec);
//		synchronized(this)
//		{
//			oidString = (String) spec.get("OID").value;
//			port = ((Integer)spec.get("Port").value).toString();
//		}
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.oidString = (String) newSpec.get("OID").strValue;
			this.port = (String) newSpec.get("Port").strValue;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
