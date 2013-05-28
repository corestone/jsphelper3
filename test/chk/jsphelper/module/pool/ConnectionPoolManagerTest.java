package chk.jsphelper.module.pool;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import chk.jsphelper.ObjectLoader;

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
		int connSize = 0;
		ConnectionPoolManager cpm = ConnectionPoolManager.getInstance();
		try
		{
			connSize = cpm.size("db");
			Connection conn = cpm.getConnection("db", "Test");
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