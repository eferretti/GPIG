package gpigb.report;

import gpigb.data.RecordSet;

import java.util.List;

public interface Reporter
{
	/**
	 * Produces the output report
	 * 
	 * @param data
	 *            The information to be formatted and displayed , can contain
	 *            record sets of sensor or analyser output
	 */
	public void generateReport(List<RecordSet<?>> data);
}
