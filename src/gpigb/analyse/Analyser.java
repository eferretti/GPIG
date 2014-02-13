package gpigb.analyse;

import gpigb.data.RecordSet;

import java.util.List;

public interface Analyser
{

	public boolean Analyse(List<RecordSet<?>> input);
	public boolean Analyse(RecordSet<?> input);
	
}
