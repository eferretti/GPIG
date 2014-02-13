package gpigb.analyse;

import gpigb.data.RecordSet;

import java.util.List;

/**
 * An Interface which must be implemented by any HUMS analyser modules.
 */
public interface Analyser
{
	/**
	 * A method which allows input of a number of sensor readings. To be used
	 * when multiple readings must be combined in order to analyse the state of
	 * the system.
	 * 
	 * @param input
	 *            A list of sets of records which may be used in analysis. Each
	 *            record set should apply to an individual sensor reading
	 *            record.
	 * @return true iff the analysis succeeds with the provided data, false
	 *         otherwise
	 */
	public boolean analyse(List<RecordSet<?>> input);

	/**
	 * A convenience method which allows input of a single set of sensor
	 * readings.
	 * 
	 * @param input
	 *            A set of records from a single sensor.
	 * @return true iff the analysis succeeds with the provided data, false
	 *         otherwise
	 */
	public boolean analyse(RecordSet<?> input);
}
