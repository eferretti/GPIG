package gpigb.configuration;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.util.Map;


public interface Configurable
{
	public void setID(int newID);
	public int getID();
	public Map<String, ConfigurationValue> getConfigSpec();
	public boolean setConfig(Map<String, ConfigurationValue> newConfig, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr);
}
