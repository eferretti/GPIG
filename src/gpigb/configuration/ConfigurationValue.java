package gpigb.configuration;

import java.io.Serializable;

public class ConfigurationValue implements Serializable
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
	public String strValue;
	public Integer intValue;
	public Float fltValue;
	
	public ConfigurationValue(ValueType type, String value)
	{
		this.type = type;
		this.strValue = value;
		this.intValue = null;
		this.fltValue = null;
	}
	
	public ConfigurationValue(ValueType type, Integer value)
	{
		this.type = type;
		this.strValue = null;
		this.intValue = value;
		this.fltValue = null;
	}
	
	public ConfigurationValue(ValueType type, Float value)
	{
		this.type = type;
		this.strValue = null;
		this.intValue = null;
		this.fltValue = value;
	}
}
