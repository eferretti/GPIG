package gpigb.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.sense.SensorObserver;

public class SimpleWebReporter extends NanoHTTPD implements SensorObserver<Float>, Reporter{

	Map<Integer, RecordSet<Float>> latestReadings = Collections.synchronizedMap(new HashMap<Integer, RecordSet<Float>>());
	
	public SimpleWebReporter(int port) 
	{
		super(port);
		try {
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Response serve(IHTTPSession session) 
	{
		try
		{
		if(session.getUri().endsWith(".js"))
			return new Response(NanoHTTPD.Response.Status.ACCEPTED, MIME_PLAINTEXT, new FileInputStream("./JS/Graph.js"));
		}catch(Exception e)
		{
			
		}
		String ret = 	"<html>" + "\n" +
						"<head>" + "\n" +
						"<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>" + "\n" +
						"<script src=\"chart.min.js\"></script>" + "\n" +
						"</head>" + "\n" +
						"<body>" + "\n" +
						"<h2>Latest Sensor Readings</h2>" + "\n" +
						"<table>" + "\n" +
						"<tr><th>Sensor ID</th><th>Latest Reading</th></tr>" + "\n";
		
		for(Integer id : latestReadings.keySet())
		{
			RecordSet<Float> rec = latestReadings.get(id);
			int len = rec.getRecordCount();
			String unit = rec.getReadingAtPosition(len-1).getMeta().get("Unit");
			ret += "<tr><td>" + id + "</td><td>" + rec.getReadingAtPosition(len-1).getData() + (unit == null ? "" : " " + unit) + "</td></tr>" + "\n";
		}
		
		ret += "</table>" + "\n";
		for(Integer id : latestReadings.keySet())
		{
			String dataString = "[";
			String labelString = "[";
			int start = Math.max(0, latestReadings.get(id).getRecordCount()-100);
			Float minReading = Float.POSITIVE_INFINITY;
			Float maxReading = Float.NEGATIVE_INFINITY;
			for(int i = start; i < latestReadings.get(id).getRecordCount(); ++i)
			{
				Float reading = latestReadings.get(id).getReadingAtPosition(i).getData();
				minReading = Math.min(minReading, reading);
				maxReading = Math.max(maxReading, reading);
				labelString += (i%50==0) ? (("\"" + latestReadings.get(id).getReadingAtPosition(i).getTimestamp()) + "\"") : "\"\"";
				dataString += ("" + latestReadings.get(id).getReadingAtPosition(i).getData());
				if(i != latestReadings.get(id).getRecordCount()-1)
				{
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
			ret += "<canvas id=\"graph_" + id + "\" width=1000 height=400></canvas>" + "\n" +
			"<script>" + "\n" +
			"ctx = $(\"#graph_" + id + "\").get(0).getContext(\"2d\");" + "\n" +
			"data = {" + "\n" +
			"labels : " + labelString + "\n" +
			"datasets : [" + "\n" +
			"{"+ "\n" +
			"	fillColor : \"rgba(220,220,220,0.5)\","+ "\n" +
			"	strokeColor : \"rgba(220,220,220,1)\","+ "\n" +
			"	pointColor : \"rgba(220,220,220,1)\","+ "\n" +
			"	pointStrokeColor : \"#fff\","+
			"	data : "+dataString + "\n" +
			"}"+ "\n" +
			"]"+ "\n" +
			"}"+ "\n" +
			"opts = {bezierCurve:true, pointDot:false, animation:false, scaleShowGridLines:false, scaleOverride:true, scaleStartValue: " + minReading + ", scaleSteps: " + steps + ", scaleStepWidth:" + stepSize + "};" +
			"new Chart(ctx).Line(data, opts);setTimeout(function(){location.reload()}, 5000);" + "\n" +
			"</script>"+ "\n";
		}
		ret +=	"</body>" + "\n" +
				"</html>";
		
		return new Response(NanoHTTPD.Response.Status.ACCEPTED, MIME_HTML, ret);
	}
	
	@Override
	public void update(int sensorID, Float reading) 
	{
		RecordSet<Float> rs = latestReadings.get(sensorID);
		if(rs == null)
		{
			Date d = Calendar.getInstance().getTime();
			rs = new RecordSet<>(d, d, sensorID);
			latestReadings.put(sensorID, rs);
		}
		rs.addRecord(new SensorRecord<Float>(sensorID, reading));
	}

	@Override
	public void generateReport(List<RecordSet<?>> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(SensorRecord<Float> reading) {
		RecordSet<Float> rs = latestReadings.get(reading.getSensorID());
		if(rs == null)
		{
			Date d = Calendar.getInstance().getTime();
			rs = new RecordSet<>(d, d, reading.getSensorID());
			latestReadings.put(reading.getSensorID(), rs);
		}
		rs.addRecord(reading);
	}
}
