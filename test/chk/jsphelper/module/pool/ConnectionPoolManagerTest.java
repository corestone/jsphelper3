package chk.jsphelper.module.pool;

/**
 * Created with IntelliJ IDEA.
 * User: Corestone
 * Date: 13. 7. 17
 * Time: 오후 2:03
 * To change this template use File | Settings | File Templates.
 */

import chk.jsphelper.ObjectLoader;
import chk.jsphelper.module.wrapper.ConnWrapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionPoolManagerTest
{
	@Before
	public void setUp () throws Exception
	{
		ObjectLoader.isSuccess();
	}

	@Test
	public void testConnectionPoolManager ()
	{
		int connSize;
		ConnectionPoolManager cpm = ConnectionPoolManager.getInstance();
		try
		{
			connSize = cpm.size("db");
			ConnWrapper conn = cpm.getConnection("db", "Test");
			assertEquals(cpm.size("db"), connSize - 1);
			cpm.releaseConnection("db", conn, "Test", true);
			assertEquals(cpm.size("db"), connSize);
			cpm.releaseConnection("db", conn, "Test", false);
			assertEquals(cpm.size("db"), connSize);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
