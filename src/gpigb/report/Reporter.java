package gpigb.report;

<<<<<<< HEAD
import gpigb.data.RecordSet;
=======
import gpigb.configuration.Configurable;
import gpigb.data.DataSet;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89

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
	public void generateReport(List<RecordSet<?>> data);
}
