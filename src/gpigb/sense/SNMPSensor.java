package gpigb.sense;

import gpigb.classloading.Patchable;
import gpigb.data.DataRecord;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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

	/**
	 * 
	 * @param oldInstance
	 */
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

			ref.get().update(new DataRecord<Float>(getID(), lastReading, "Unit", "MB"));
		}
	}

	@Override
	public void run()
	{
		Snmp snmp = null;

		PDU req = new PDU();
		req.setType(PDU.GET);

		OID oid = new OID(oidString);
		req.add(new VariableBinding(oid));

		CommunityTarget target = new CommunityTarget();

		Address a = new UdpAddress("localhost/" + port);
		target.setAddress(a);
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

		while (true) {
			try {
				ResponseEvent responseEvent = snmp.send(req, target);

				PDU responsePDU = responseEvent.getResponse();
				VariableBinding binding = responsePDU.getVariableBindings().get(0);
				lastReading = Float.valueOf(binding.getVariable().toString());
				lastReading /= (1000 * 1000); // Convert to MB

				notifyObservers();

				Thread.sleep(250);
			}
			catch (Exception e) {
			}
		}
	}

}
