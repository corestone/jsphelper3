package chk.jsphelper.util;

import java.io.Closeable;

import chk.jsphelper.module.wrapper.ConnWrapper;

/**
 * @author Corestone
 */
public class CloseUtil
{
	/**
	 * @param object
	 */
	public static void closeObject (Closeable object)
	{
		try
		{
			if (object != null)
			{
				object.close();
				object = null;
			}
		}
		catch (final Exception e)
		{
		}
	}

	/**
	 * @param conn
	 * @param commit
	 */
	public static void closeObject (ConnWrapper conn, final boolean commit)
	{
		try
		{
			if (commit)
			{
				conn.commit();
			}
			else
			{
				conn.rollback();
			}
		}
		catch (final Exception e)
		{
		}

		try
		{
			if (conn != null)
			{
				conn.close();
				conn = null;
			}
		}
		catch (final Exception e)
		{
		}
	}

	private CloseUtil ()
	{
	}
}