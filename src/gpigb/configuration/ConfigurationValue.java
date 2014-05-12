package gpigb.configuration;

public class ConfigurationValue
{
	public static enum ValueType
	{
		String,
		Integer,
		Float,
		Analyser,
		Reporter,
		Sensor,
		Store,
		OutStream,
		InStream
	}
	
	public final ValueType type;
	public Object value;
	
	public ConfigurationValue(ValueType type, Object value)
	{
		this.type = type;
		this.value = value;
	}
}
