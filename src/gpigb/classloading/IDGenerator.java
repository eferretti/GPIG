package gpigb.classloading;

public class IDGenerator
{
	private static int nextID = 1;
	
	public static int getNextID()
	{
		synchronized (IDGenerator.class)
		{
			return nextID++;
		}
	}
	
	public static void setMinID(int minID)
	{
		synchronized (IDGenerator.class)
		{
			nextID = minID+1;
		}
	}
}
