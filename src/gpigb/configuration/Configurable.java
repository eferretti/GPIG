package gpigb.configuration;

import java.util.Map;


public interface Configurable
{
	public void setID(int newID);
	public int getID();
	public Map<String, ConfigurationValue> getConfigSpec();
	public boolean setConfig(Map<String, ConfigurationValue> newConfig);
}
