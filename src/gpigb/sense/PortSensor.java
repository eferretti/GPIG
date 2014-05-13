package gpigb.sense;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;

public class PortSensor implements Sensor<Double>, Runnable {
	
	private ArrayList<SensorObserver<Double>> observers;
	private Double lastReading;
	private boolean active;
	private Socket clientSocket;
	private PrintWriter outSocketStream;
	private BufferedReader inSocketStream;
	private String hostName;
	private Integer portNumber;
	
	public void PortSensor()
	{
		hostName = "localhost";
		portNumber = 4444;
		observers = new ArrayList<SensorObserver<Double>>();
		new Thread(this).start();
	}
	
	public void run()
	{
		Double reading;
		try {
				clientSocket = new Socket(hostName, portNumber);
		        outSocketStream = new PrintWriter(clientSocket.getOutputStream(), true);
		        inSocketStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		        	        
		        while ((reading = Double.parseDouble(inSocketStream.readLine())) != null) {
		        	lastReading = reading;
		        	System.out.println("echo: " + reading);
//		        	this.notifyObservers();
		        }
	        
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + hostName);
	            System.exit(1); 	
	        } catch (IOException e) {
	        	System.err.println("Error. Port Stream IO " + hostName);
				e.printStackTrace();
			}
		
		
	}
	
	@Override
	public synchronized void configure(ConfigurationHandler handler)
	{ 
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		
		configSpec.put("HostName", new ConfigurationValue(ValueType.String, null ));
		configSpec.put("Port", new ConfigurationValue(ValueType.Integer, null ));
		
		handler.getConfiguration(configSpec);
		
		this.hostName = (String) configSpec.get("HostName").value;
		this.portNumber = (Integer) configSpec.get("Port").value;
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
	public void registerObserver(SensorObserver<Double> obs) {
		observers.add(obs);
	}

	@Override
	public void removeObserver(SensorObserver<Double> obs) {
		observers.remove(obs);
		
	}

	@Override
	public void notifyObservers() {
		
		Iterator<SensorObserver<Double>> it = observers.iterator();
		while (it.hasNext()) {
			it.next().update(id, lastReading);
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
}
