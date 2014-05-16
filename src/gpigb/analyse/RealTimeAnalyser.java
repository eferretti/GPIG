package gpigb.analyse;

import gpigb.configuration.Configurable;
import gpigb.data.RecordSet;
import gpigb.sense.SensorObserver;

import java.util.List;

/**
 * An Interface which must be implemented by any HUMS analyser modules.
 */
public interface RealTimeAnalyser extends Analyser, SensorObserver
{

}
