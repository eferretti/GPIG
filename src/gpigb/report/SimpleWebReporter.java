package gpigb.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.store.Store;

/**
 * A reporter which presents a small web server to allow web viewing of system
 * performance
 */
public class SimpleWebReporter extends NanoHTTPD implements Reporter
{
	StrongReference<Store> store;

	public SimpleWebReporter()
	{
		super(8080);
		try {
			this.start();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Response serve(IHTTPSession session)
	{
		try {
			if (session.getUri().endsWith(".js"))
				return new Response(NanoHTTPD.Response.Status.ACCEPTED, MIME_PLAINTEXT, new FileInputStream(
						"./JS/Graph.js"));
		}
		catch (Exception e) {

		}
		String ret = "<html>" + "\n" + "<head>" + "\n"
				+ "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>" + "\n"
				+ "<script src=\"chart.min.js\"></script>" + "\n" + "</head>" + "\n" + "<body>" + "\n"
				+ "<h2>Latest Sensor Readings</h2>" + "\n" + "<table>" + "\n"
				+ "<tr><th>Sensor ID</th><th>Latest Reading</th></tr>" + "\n";

		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		cal.add(Calendar.MINUTE, -5);
		Date startDate = cal.getTime();

		RecordSet<Float> data = new RecordSet<>(startDate, endDate, 0);
		
		store.get().read(data);

		int len = data.getRecordCount();
		Dictionary<String, String> meta = data.getDataAtPosition(len-1).getMeta();
		String unit = "";
		try
		{
			unit = meta.get("Unit");
		}
		catch(Exception e)
		{
			
		}
		
		ret += "<tr><td>Latest Reading</td><td>" + data.getDataAtPosition(len - 1).getData()
				+ (unit == null ? "" : " " + unit) + "</td></tr>" + "\n";

		ret += "</table>" + "\n";
		String dataString = "[";
		String labelString = "[";
		int start = 0;
		Float minReading = Float.POSITIVE_INFINITY;
		Float maxReading = Float.NEGATIVE_INFINITY;
		for (int i = start; i < data.getRecordCount(); i+=50) {
			Float reading = new Float(data.getDataAtPosition(i).getData());
			minReading = Math.min(minReading, reading);
			maxReading = Math.max(maxReading, reading);
			labelString += (i % 200 == 0) ? (("\"" + data.getDataAtPosition(i).getTimestamp()) + "\"") : "\"\"";
			dataString += ("" + data.getDataAtPosition(i).getData());
			if (i != data.getRecordCount() - 1) {
				dataString += ",";
				labelString += ",";
			}
		}

		minReading = minReading - (maxReading * 0.001f);
		maxReading = maxReading + (maxReading * 0.001f);

		int steps = 10;
		float stepSize = maxReading - minReading;
		stepSize /= steps;

		dataString += "],";
		labelString += "],";
		ret += "<canvas id=\"graph" + "\" width=1000 height=600></canvas>" + "\n" + "<script>" + "\n"
				+ "ctx = $(\"#graph" + "\").get(0).getContext(\"2d\");" + "\n" + "data = {" + "\n" + "labels : "
				+ labelString
				+ "\n"
				+ "RecordSets : ["
				+ "\n"
				+ "{"
				+ "\n"
				+ "	fillColor : \"rgba(220,220,220,0.5)\","
				+ "\n"
				+ "	strokeColor : \"rgba(220,220,220,1)\","
				+ "\n"
				+ "	pointColor : \"rgba(220,220,220,1)\","
				+ "\n"
				+ "	pointStrokeColor : \"#fff\","
				+ "	data : "
				+ dataString
				+ "\n"
				+ "}"
				+ "\n"
				+ "]"
				+ "\n"
				+ "}"
				+ "\n"
				+ "opts = {bezierCurve:true, pointDot:false, animation:false, scaleShowGridLines:false, scaleOverride:true, scaleStartValue: "
				+ minReading
				+ ", scaleSteps: "
				+ steps
				+ ", scaleStepWidth:"
				+ stepSize
				+ "};"
				+ "new Chart(ctx).Line(data, opts);setTimeout(function(){location.reload()}, 2000);"
				+ "\n"
				+ "</script>" + "\n";

		ret += "</body>" + "\n" + "</html>";

		return new Response(NanoHTTPD.Response.Status.ACCEPTED, MIME_HTML, ret);
	}

	@Override
	public void generateReport(List<RecordSet<?>> data)
	{
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		HashMap<String, ConfigurationValue> configSpec = new HashMap<>();
		configSpec.put("Store", new ConfigurationValue(ValueType.Store, store != null ? store.get().getID() : 0));
		return configSpec;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			store = stMgr.getObjectByID(newSpec.get("Store").intValue);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private int id;
	public void setID(int newID)
	{
		this.id = newID;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	@Override
	public int getConfigurationStepNumber() {
		
		return 1;
	}
}
