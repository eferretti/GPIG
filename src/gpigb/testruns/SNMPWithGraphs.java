package gpigb.testruns;

import gpigb.external.HUMSSystem;
import gpigb.external.HUMSSystemImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class SNMPWithGraphs
{
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException
	{
		HUMSSystem sys = new HUMSSystemImpl();
		
		// Open a registry to allow remote access
		try 
		{
            LocateRegistry.createRegistry(1099); 
        }
		catch (RemoteException e) 
		{
        }

		try
		{
			Naming.rebind("HUMS", sys);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		while(true)
		{
			Thread.sleep(10);
		}
	}
}
