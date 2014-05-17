package gpigb.scheduler;

import gpigb.analyse.Analyser;
import gpigb.classloading.StrongReference;
import gpigb.data.RecordSet;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.PriorityBlockingQueue;

import gpigb.scheduler.Scheduler.SchedulerTask;

public class Scheduler implements Runnable, Comparator<SchedulerTask>
{
	PriorityBlockingQueue<SchedulerTask> queue = new PriorityBlockingQueue<SchedulerTask>(1, this);
	Calendar cal = new GregorianCalendar();	
	
	public Scheduler()
	{
		new Thread(this).start();
	}

	@Override
	public void run()
	{
		while(!false)
		{
			if(queue.isEmpty())
				continue;
			
			Date now = Calendar.getInstance().getTime();
			if(now.before(queue.peek().nextRun))
				continue;
			
			SchedulerTask t = queue.poll();
			t.run();
			
			queue.add(t.next());
		}
	}
	
	public void addSensorTask(Date firstRun, int msTick, StrongReference<Sensor> sensor)
	{
		queue.add(new SensorSchedulerTask(firstRun, msTick, sensor));
	}
	
	public void addReporterTask(Date firstRun, int msTick, StrongReference<Reporter> reporter)
	{
		queue.add(new ReporterSchedulerTask(firstRun, msTick, reporter));
	}
	
	public void addAnalyserTask(Date firstRun, int msTick, StrongReference<Analyser> analyser)
	{
		queue.add(new AnalyserSchedulerTask(firstRun, msTick, analyser));
	}
	
	public static abstract class SchedulerTask
	{
		public final Date nextRun;
		public final long msPeriod;
		
		private SchedulerTask(Date firstRun, long msPeriod)
		{
			this.nextRun = firstRun;
			this.msPeriod = msPeriod;
		}
		
		public abstract void run();
		public abstract SchedulerTask next();
	}
	
	public static class SensorSchedulerTask extends SchedulerTask
	{
		private final StrongReference<Sensor> target;
		
		public SensorSchedulerTask(Date firstRun, long msPeriod, StrongReference<Sensor> target)
		{
			super(firstRun, msPeriod);
			this.target = target;
		}

		@Override
		public void run()
		{
			this.target.get().poll();
		}

		@Override
		public SchedulerTask next()
		{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MILLISECOND, (int) msPeriod);
			return new SensorSchedulerTask(c.getTime(), msPeriod, target);
		}
	}
	
	public static class AnalyserSchedulerTask extends SchedulerTask
	{
		private final StrongReference<Analyser> target;
		
		public AnalyserSchedulerTask(Date firstRun, long msPeriod, StrongReference<Analyser> target)
		{
			super(firstRun, msPeriod);
			this.target = target;
		}

		@Override
		public void run()
		{
			this.target.get().analyse((RecordSet<?>)null);
		}

		@Override
		public SchedulerTask next()
		{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MILLISECOND, (int) msPeriod);
			return new AnalyserSchedulerTask(c.getTime(), msPeriod, target);
		}
	}
	
	public static class ReporterSchedulerTask extends SchedulerTask
	{
		private final StrongReference<Reporter> target;
		
		public ReporterSchedulerTask(Date firstRun, long msPeriod, StrongReference<Reporter> target)
		{
			super(firstRun, msPeriod);
			this.target = target;
		}

		@Override
		public void run()
		{
			this.target.get().generateReport(null);
		}

		@Override
		public SchedulerTask next()
		{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MILLISECOND, (int) msPeriod);
			return new ReporterSchedulerTask(c.getTime(), msPeriod, target);
		}
	}

	@Override
	public int compare(SchedulerTask o1, SchedulerTask o2)
	{
		return o1.nextRun.before(o2.nextRun) ? -1 : 1;
	}
}
