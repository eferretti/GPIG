import gpigb.analyse.AccelerationAnalyser;
import gpigb.analyse.Analyser;
import gpigb.analyse.ThresholdAnalyser;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
import gpigb.report.OutOfRangeReporter;
import gpigb.report.Reporter;
import gpigb.report.UnexpectedAccelerationReporter;
import gpigb.sense.ConcreteSensorOne;
import gpigb.sense.ConcreteSensorTwo;
import gpigb.sense.SensorObserver;
import gpigb.store.InMemoryStore;
import gpigb.store.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestImplJSON
{
	public static void main(String[] args)
	{
		ConcreteSensorOne s1 = new ConcreteSensorOne();
		ConcreteSensorTwo s2 = new ConcreteSensorTwo();

		final Store store = new InMemoryStore();

		final Analyser atemp = new ThresholdAnalyser(40, 20);
		final Analyser aelev = new ThresholdAnalyser(1000, 1100);
		final Analyser a2 = new AccelerationAnalyser();

		final Reporter r1 = new OutOfRangeReporter();
		final Reporter r2 = new UnexpectedAccelerationReporter();

		s1.registerObserver(new SensorObserver<Integer>()
		{
			@Override
			public void update(int sensorID, Integer reading)
			{
				DataSet<Integer> rs = new DataSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new DataRecord<Integer>(1, reading));
				store.write(rs);
			}

			@Override
			public void update(DataRecord<Integer> reading)
			{
				// TODO Auto-generated method stub

			}
		});

		s2.registerObserver(new SensorObserver<Integer>()
		{
			@Override
			public void update(int sensorID, Integer reading)
			{
				DataSet<Integer> rs = new DataSet<>(new Date(), new Date(), sensorID);
				rs.addRecord(new DataRecord<Integer>(2, reading));
				store.write(rs);
			}

			@Override
			public void update(DataRecord<Integer> reading)
			{
				// TODO Auto-generated method stub

			}
		});

		(new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (true) {
					System.out.println("Looping...");
					try {
						Thread.sleep(1000);
					}
					catch (Exception e) {
					}

					Calendar c = Calendar.getInstance();
					c.set(2010, 1, 1, 1, 1);
					Date d1 = c.getTime();
					c.set(2015, 1, 1, 1, 1);
					Date d2 = c.getTime();
					DataSet<Integer> rs1 = new DataSet<Integer>(d1, d2, 1);
					store.read(rs1);
					atemp.analyse(rs1);
					DataSet<Integer> rs2 = new DataSet<Integer>(d1, d2, 2);
					store.read(rs2);
					aelev.analyse(rs2);
					List<DataSet<?>> rc = new ArrayList<DataSet<?>>();
					rc.add(rs1);
					rc.add(rs2);
					a2.analyse(rc);

				}
			}
		})).start();
	}

}
