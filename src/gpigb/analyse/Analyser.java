package gpigb.analyse;

import gpigb.data.RecordSet;
import java.util.*;

public interface Analyser
{
	public boolean Analyse(RecordSet<?> input);
	public boolean Analyse(List<RecordSet<?>> input);
}
