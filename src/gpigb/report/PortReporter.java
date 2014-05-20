package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.sense.SensorObserver;
import gpigb.store.Store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortReporter implements Reporter {

	private int id;

	private ArrayList<SensorObserver> observers;
	private Double lastReading;
	private boolean active;
	private Socket clientSocket;
	private PrintWriter outSocketStream;
	private BufferedReader inSocketStream;
	private String hostName;
	private Integer portNumber;
	private boolean blocking = false;
	
	
	public PortReporter()
	{
		hostName = "localhost";
		portNumber = 4440;
		
		
	}
	
	@Override
	public void setID(int newID) {
		id = newID;

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public int getConfigurationStepNumber() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec() {
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		
		configSpec.put("Host", new ConfigurationValue(ValueType.String, hostName));
		configSpec.put("Port", new ConfigurationValue(ValueType.Integer, portNumber));

		return configSpec;
	}

	@Override
	public boolean setConfig(Map<String, ConfigurationValue> newSpec,
			ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr,
			ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr) {
		try {
//			this.hostName = (String) newSpec.get("HostName").strValue;
			this.portNumber = (Integer) newSpec.get("Port").intValue;
			
			if(inSocketStream != null)
				inSocketStream.close();

			if(outSocketStream != null)
			outSocketStream.close();

			if(clientSocket != null)
				clientSocket.close();
			
			clientSocket = new Socket(hostName, portNumber);    
            outSocketStream = new PrintWriter(clientSocket.getOutputStream(), true); 
			
			
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
	public void generateReport(List<RecordSet<?>> data) {
		System.out.println("Mode change should happen here");
		outSocketStream.println("1");

	}

}
