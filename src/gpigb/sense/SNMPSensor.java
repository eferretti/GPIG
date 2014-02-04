package gpigb.sense;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import gpigb.classloading.Patchable;

public class SNMPSensor extends Patchable implements Sensor<Integer>, Runnable
{
	private ArrayList<WeakReference<SensorObserver<Integer>>> observers = new ArrayList<>();
	private Integer lastReading = null;
	
	//JVM Heap Used
	private String oid = "1.3.6.1.4.1.42.2.145.3.163.1.1.2.11";
	
	// Default SNMP settings
	private String port = "19004";
	
	public SNMPSensor()
	{
		try
		{
			Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
			snmp.listen();
			Address a = new UdpAddress("localhost/"+port);
			CommunityTarget target = new CommunityTarget();
			target.setAddress(a);
			target.setTimeout(500);
			target.setRetries(3);
			target.setCommunity(new OctetString("public"));
			target.setVersion(SnmpConstants.version2c);
			
			PDU req = new PDU();
			req.setType(PDU.GET);
			OID oid = new OID(this.oid);
			req.add(new VariableBinding(oid));
			
			PDU responsePDU = null;
			ResponseEvent rEv = snmp.send(req, target);
			
			if(rEv != null)
			{
				responsePDU = rEv.getResponse();
                if ( responsePDU != null)
                {
                                
                    Vector<? extends VariableBinding> tmpv = responsePDU.getVariableBindings();
                    if(tmpv != null)
                    {
                        for(int k=0; k <tmpv.size();k++)
                        {
                            VariableBinding vb = (VariableBinding) tmpv.get(k);
                            String output = null;
                            if ( vb.isException())
                            {

                                String errorstring = vb.getVariable().getSyntaxString();
                                System.out.println("Error:"+errorstring);
                            }
                            else
                            {
                                String sOid = vb.getOid().toString();
                                Variable var = vb.getVariable();
                                OctetString oct = new OctetString((OctetString)var);
                                String sVar =  oct.toString();

                                System.out.println("success:"+sVar);
                            }

                        
                        }
                    
                    }
                }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
	public Integer poll()
	{
		return lastReading;
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void registerObserver(SensorObserver<Integer> obs)
	{
		for(WeakReference ref:observers)
		{
			if(ref.get() == obs)
			{
				return;
			}
		}
		
		observers.add(new WeakReference<SensorObserver<Integer>>(obs));
	}

	@Override
	public void removeObserver(SensorObserver<Integer> obs)
	{
		for(WeakReference ref : observers)
		{
			if(ref.get() == obs)
			{
				observers.remove(ref);
			}
		}
	}

	@Override
	public void notifyObservers()
	{
		ArrayList<WeakReference<SensorObserver<Integer>>> toRemove = new ArrayList<>();
		
		for(WeakReference<SensorObserver<Integer>> ref:observers)
		{
			if(ref.get() == null)
			{
				toRemove.add(ref);
				continue;
			}
			
			ref.get().update(getID(), lastReading);
		}
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		
	}

}
