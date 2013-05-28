package chk.jsphelper.module.runnable;

import chk.jsphelper.Constant;
import chk.jsphelper.module.pool.ThreadPool;

public abstract class AbstractRunnable implements Runnable
{
	private int interval = -1;

	protected AbstractRunnable (final int interval)
	{
		this.interval = interval;
	}

	abstract public void run ();

	protected boolean sleepInterval ()
	{
		if (this.interval <= 0)
		{
			return false;
		}
		try
		{
			for (int i = 0, z = this.interval; i < z; i++)
			{
				if (ThreadPool.getInstance().isEnd())
				{
					return false;
				}
				Thread.sleep(1000);
			}
		}
		catch (final InterruptedException ie)
		{
			Constant.getLogger().error("{} 스레드에서 sleep메소드에 대한 에러 발생", this.getClass().getName(), ie);
		}
		catch (final Exception e)
		{
			return false;
		}
		return true;
	}
}