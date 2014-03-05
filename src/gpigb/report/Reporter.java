package gpigb.report;

import gpigb.base.Configurable;
import gpigb.data.DataSet;

import java.util.List;

public interface Reporter extends Configurable
{
	/**
	 * Produces the output report
	 * 
	 * @param data
	 *            The information to be formatted and displayed , can contain
	 *            record sets of sensor or analyser output
	 */
	public void generateReport(List<DataSet<?>> data);
}
