package gpigb.sense;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.report.Reporter;
import gpigb.store.Store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PortSensor implements Sensor<Double>, Runnable {
	
	private ArrayList<SensorObserver> observers;
	private Double lastReading;
	private boolean active;
	private Socket clientSocket;
	private PrintWriter outSocketStream;
	private BufferedReader inSocketStream;
	private String hostName;
	private Integer portNumber;
	private boolean blocking = false;
	
	public PortSensor()
	{
		hostName = "localhost";
		portNumber = 4440;
		observers = new ArrayList<SensorObserver>();
		new Thread(this).start();
	}
	
	public void run()
	{
		Double reading;
		try 
		{
			clientSocket = new Socket(hostName, portNumber);
	        outSocketStream = new PrintWriter(clientSocket.getOutputStream(), true);
	        inSocketStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} 
		catch (UnknownHostException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true)
		{
			try 
			{
		        while ((reading = Double.parseDouble(inSocketStream.readLine())) != null) 
		        {
		        	lastReading = reading;
		        	System.out.println("echo: " + reading);
		        	this.notifyObservers();
		        }
	        } 
			catch (UnknownHostException e) 
			{
	            System.err.println("Don't know about host " + hostName);
	        }
			catch (IOException e) 
			{
	        	System.err.println("Error. Port Stream IO " + hostName);
//				System.out.println("Pre");
	        	e.printStackTrace();
//				System.out.println("Post");	
			}
			catch (Exception e) 
			{
//				System.err.println("Error. NullPtr?");
//				e.printStackTrace();
			}
//			System.out.println("Post Post");
			
			try
			{
//				System.out.println("Looping");
			}
			catch (Exception e) 
			{
				System.err.println("Error. Insomnia" + hostName);
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{ 
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		
		configSpec.put("HostName", new ConfigurationValue(ValueType.String, hostName));
		configSpec.put("Port", new ConfigurationValue(ValueType.Integer, portNumber));

		return configSpec;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		
		try {
			this.hostName = (String) newSpec.get("HostName").strValue;
			this.portNumber = (Integer) newSpec.get("Port").intValue;
			
			if(inSocketStream != null)
				inSocketStream.close();

			if(outSocketStream != null)
			outSocketStream.close();

			if(clientSocket != null)
				clientSocket.close();
			
			clientSocket = new Socket(hostName, portNumber);
			outSocketStream = new PrintWriter(clientSocket.getOutputStream(), true);
		    inSocketStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            return false;
        } catch (IOException e) {
        	System.err.println("Error. Port Stream IO " + hostName);
			e.printStackTrace();
			return false;
		} catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
       
		return true;
	}

	@Override
	public Double poll() {
		// TODO Auto-generated method stub
		return lastReading;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}

	@Override
	public void registerObserver(SensorObserver obs) {
		observers.add(obs);
	}

	@Override
	public void removeObserver(SensorObserver obs) {
		observers.remove(obs);
		
	}

	@Override
	public void notifyObservers() {
		
		Iterator<SensorObserver> it = observers.iterator();
		while (it.hasNext()) {
			if(!it.next().update(id, lastReading.intValue()))
			{
				System.out.println("Error Sensor Reading not stored.");
			}
		}
		
	}


	private int id;
	public void setID(int newID)
	{
		this.id = newID;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	@Override
	public int getConfigurationStepNumber() {
		
		return 1;
	}
}
