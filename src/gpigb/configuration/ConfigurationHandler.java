package gpigb.configuration;

import java.util.Map;

/**
 * Responsible for configuring a module. Called with a map which contains key
 * and (possibly null) default values. Upon returning the configSpec Map should
 * be populated.
 */
public interface ConfigurationHandler
{
	public void getConfiguration(Map<String, ConfigurationValue> configSpec);
}
