package gpigb.store;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.Patchable;
import gpigb.configuration.ConfigurationValue;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;

/**
 * A file store which connects to a locally running Mongo NoSQL store on the
 * default port.
 */
public class MongoStore extends Patchable implements Store
{
	Mongo client;
	DB db;
	DBCollection readings;

	public MongoStore()
	{
		try {
			client = new Mongo("localhost", 27017);
			db = client.getDB("HUMS");
			readings = db.getCollection("Readings");
		}
		catch (Exception e) {
		}
	}

	public MongoStore(Object oldInstance)
	{
		super(oldInstance);
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Override
	public boolean read(RecordSet<?> unpopulated)
	{
		Date fromDate = unpopulated.getFromTime();
		Date toDate = unpopulated.getToTime();
		DBObject query = QueryBuilder.start().put("SensorID").is(unpopulated.getSensorID()).put("Timestamp")
				.lessThanEquals(toDate).greaterThanEquals(fromDate).get();

		DBCursor cursor = readings.find(query);
		for (DBObject o : cursor) {
			Integer id = (Integer)o.get("SensorID");
			Object data = o.get("SensorID");
			Date timestamp = (Date) o.get("Timestamp");
			SensorRecord r = new SensorRecord<>(id, data);
			r.setDateTime(timestamp);
			unpopulated.addRecord(r);
		}

		return true;
	}

	@Override
	public boolean write(RecordSet<?> data)
	{
		ArrayList<DBObject> objects = new ArrayList<>();

		for (int i = 0; i < data.getRecordCount(); ++i) {
			BasicDBObject obj = new BasicDBObject();
			SensorRecord<?> rec = data.getDataAtPosition(i);
			obj.append("SensorID", rec.getSensorID());
			obj.append("Timestamp", rec.getTimestamp());
			obj.append("Value", rec.getData());
			obj.append("Meta", rec.getMeta());

			objects.add(obj);
		}

		readings.insert(objects);

		return true; // Cos why not?
	}

	@Override
	public boolean delete(RecordSet<?> items)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized Map<String, ConfigurationValue> getConfigSpec()
	{
		return null;
	}
	
	public synchronized boolean setConfig(Map<String, ConfigurationValue> newSpec, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		return true;
	}

	@Override
	public boolean update(int sensorID, Integer reading) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(int sensorID, Double reading) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(int sensorID, String reading) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(SensorRecord<?> reading) {
		// TODO Auto-generated method stub
		return false;
	}

}
