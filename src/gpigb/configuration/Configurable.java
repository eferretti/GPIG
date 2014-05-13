package gpigb.configuration;


public interface Configurable
{
	public void setID(int newID);
	public int getID();
	public void configure(ConfigurationHandler handler);
}
