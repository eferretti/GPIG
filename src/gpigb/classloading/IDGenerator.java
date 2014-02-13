package gpigb.classloading;

/**
 * A purely static class which is used to generated system-wide unique IDs
 */
public class IDGenerator
{
	private static int nextID = 1;

	public static int getNextID()
	{
		synchronized (IDGenerator.class) {
			return nextID++;
		}
	}

	public static void setMinID(int minID)
	{
		synchronized (IDGenerator.class) {
			nextID = minID + 1;
		}
	}
}
