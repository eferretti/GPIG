package gpigb.classloading;

/**
 * Creates a strong reference to an object which will be retained in memory and 
 * protected from the garbage collector. Used to allow runtime class upgrades.
 *
 * @param <Interface> The type of object being referenced
 */
public class StrongReference<Interface>
{
	protected Interface object = null;
	
	/**
	 * Create a reference to the object provided
	 * @param object The object to reference
	 */
	public StrongReference(Interface object)
	{
		this.object = object;
	}
	
	/**
	 * Obtain the object which is being referenced
	 * @return The referenced object
	 */
	public Interface get()
	{
		return this.object;
	}
}
