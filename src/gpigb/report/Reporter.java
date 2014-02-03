package gpigb.report;

import java.util.*;
import gpigb.data.*;
public interface Reporter
{
	/**
	 * Function Produces the output report 
	 * 
	 * @param data The information to be formatted and displayed , can contain record sets of 
	 * 				sensor or analyser output
	 */
	public void GenerateReport(List<RecordSet<?>> data );
	
}
