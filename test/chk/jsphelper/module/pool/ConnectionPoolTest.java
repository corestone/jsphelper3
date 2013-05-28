package chk.jsphelper.module.pool;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import chk.jsphelper.ObjectFactory;
import chk.jsphelper.ObjectLoader;

public class ConnectionPoolTest
{
	@Before
	public void setUp () throws Exception
	{
		ObjectLoader.isSuccess();
	}

	@Test
	public void testConnectionPool ()
	{
		ConnectionPool cp = new ConnectionPool(ObjectFactory.getDataSource("db"));
		assertNotNull(cp);
		assertTrue(cp.size() > 0);
		try
		{
			Connection conn = cp.getConnection("Test");
			System.out.println(conn.getCatalog() + " size : " + cp.size());
			cp.releaseConnection(conn, "Test", true);
			System.out.println(conn.getCatalog() + " size : " + cp.size());
			cp.releaseConnection(conn, "Test", false);
			System.out.println(conn.getCatalog() + " size : " + cp.size());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
