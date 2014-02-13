package gpigb.sense;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
/**
 * 
 *  A simulated sensor which generates random integer values between -5000 and 20000
 *
 */
public class ConcreteSensorOne implements Sensor<Integer>, Runnable
{

	private ArrayList<SensorObserver<Integer>> observers;
	private Integer currentReading;
	private int id = 1;

	private Integer min = -5000;
	private Integer max = 20000;
	
	public ConcreteSensorOne()
	{
		observers = new ArrayList<SensorObserver<Integer>>();
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			currentReading = this.generateRandom(min, max);
			notifyObservers();
			try
			{
				// sleep for 2 seconds
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				// Consider calling Thread.currentThread().interrupt(); here.
			}
		}
	}

	@Override
	public int getID()
	{
		return id;
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
		while (it.hasNext())
		{
			it.next().update(id, currentReading);
		}
	}
	/**
	 * Generates a random integer in range
	 * @param min minimum value of generated number 
	 * @param max maximum value of generated number 
	 * @return generated number
	 */
	private Integer generateRandom(Integer min, Integer max)
	{
		Random rand = new Random();
		int value = rand.nextInt((max - min) + 1) + min;
		return value;
	}

}
