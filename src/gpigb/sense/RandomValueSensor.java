package gpigb.sense;

import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
/**
 * 
 *  A simulated sensor which generates random integer values between 0 and 15000
 *
 */
public class RandomValueSensor implements Sensor<Integer>, Runnable
{

	private ArrayList<SensorObserver<Integer>> observers;
	private Integer currentReading;

	private Integer min = 0;
	private Integer max = 15000;

	public RandomValueSensor()
	{
		observers = new ArrayList<SensorObserver<Integer>>();
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		while (true) {
			currentReading = this.generateRandom(min, max);
			notifyObservers();
			try {
				// sleep for 2 seconds
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				// Consider calling Thread.currentThread().interrupt(); here.
			}
		}
	}

	@Override
	public Integer poll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void registerObserver(SensorObserver<Integer> obs)
	{
		observers.add(obs);
	}

	@Override
	public void removeObserver(SensorObserver<Integer> obs)
	{
		observers.remove(obs);
	}

	@Override
	public void notifyObservers()
	{
		Iterator<SensorObserver<Integer>> it = observers.iterator();
		while (it.hasNext()) {
			it.next().update(id, currentReading);
		}
	}
	/**
	 * Generates a random integer in range
	 * @param min minimum value of generated number 
	 * @param max maximum value of generated number 
	 * @return generated number
	 */
	private synchronized Integer generateRandom(Integer min, Integer max)
	{
		Random rand = new Random();
		int value = rand.nextInt((max - min) + 1) + min;
		return value;
	}

	@Override
	public synchronized void configure(ConfigurationHandler handler)
	{ 
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		
		configSpec.put("Min", new ConfigurationValue(ValueType.Integer, Integer.MIN_VALUE));
		configSpec.put("Max", new ConfigurationValue(ValueType.Integer, Integer.MAX_VALUE));
		
		handler.getConfiguration(configSpec);
		
		this.min = (Integer) configSpec.get("Min").value;
		this.max = (Integer) configSpec.get("Max").value;
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