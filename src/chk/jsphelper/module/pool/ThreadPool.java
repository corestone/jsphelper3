package chk.jsphelper.module.pool;

import chk.jsphelper.Constant;
import chk.jsphelper.module.runnable.AbstractRunnable;

import java.util.ArrayList;
import java.util.Collection;

public class ThreadPool
{
	private class WorkerThread extends Thread
	{
		public ThreadPool owner;

		WorkerThread (final ThreadPool o)
		{
			this.owner = o;
		}

		@Override
		public void run ()
		{
			Runnable target = null;
			while (!this.owner.isEnd())
			{
				try
				{
					target = this.owner.getAssignment();
					if (target != null)
					{
						target.run();
						this.owner.workerEnd();
					}
				}
				catch (final Exception e)
				{
					Constant.getLogger().error(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	private static ThreadPool instance = null;

	public static ThreadPool getInstance ()
	{
		if (ThreadPool.instance == null)
		{
			ThreadPool.instance = new ThreadPool();
		}
		return ThreadPool.instance;
	}

	private int activeThreads = 0;
	private Collection<AbstractRunnable> assignments = null;
	private boolean end = false;
	private boolean started = false;

	private Thread[] threads = null;

	private ThreadPool ()
	{
		final int threadSize = Integer.parseInt(Constant.getValue("ThreadPool.Size", "8"));
		if (this.assignments == null)
		{
			this.assignments = new ArrayList<AbstractRunnable>(threadSize);
		}
		if (this.threads == null)
		{
			this.threads = new WorkerThread[threadSize];
			for (int i = 0, z = this.threads.length; i < z; i++)
			{
				this.threads[i] = new WorkerThread(this);
				this.threads[i].start();
			}
		}
	}

	public synchronized void assign (final AbstractRunnable r)
	{
		this.workerBegin();
		this.assignments.add(r);
		this.notify();
	}

	public void complete ()
	{
		this.waitBegin();
		this.waitDone();
	}

	public synchronized void destroy ()
	{
		this.end = true;
		this.finalize();
	}

	public boolean isEnd ()
	{
		return this.end;
	}

	@Override
	protected void finalize ()
	{
		try
		{
			super.finalize();
		}
		catch (Throwable t)
		{
		}
		this.reset();
		for (final Thread element : this.threads)
		{
			element.interrupt();
			this.workerEnd();
		}
		this.waitDone();
	}

	private synchronized Runnable getAssignment ()
	{
		try
		{
			while ((this.assignments != null) && !this.assignments.iterator().hasNext())
			{
				this.wait();
			}
			if (this.assignments != null)
			{
				final Runnable r = this.assignments.iterator().next();
				this.assignments.remove(r);
				return r;
			}
			return null;
		}
		catch (final InterruptedException e)
		{
			this.workerEnd();
			return null;
		}
	}

	private synchronized void reset ()
	{
		this.activeThreads = 0;
	}

	private synchronized void waitBegin ()
	{
		try
		{
			while (!this.started)
			{
				this.wait();
			}
		}
		catch (final InterruptedException e)
		{
		}
	}

	private synchronized void waitDone ()
	{
		try
		{
			while (this.activeThreads > 0)
			{
				this.wait();
			}
		}
		catch (final InterruptedException e)
		{
		}
	}

	private synchronized void workerBegin ()
	{
		this.activeThreads++;
		this.started = true;
		this.notify();
	}

	private synchronized void workerEnd ()
	{
		this.activeThreads--;
		this.notify();
	}
}