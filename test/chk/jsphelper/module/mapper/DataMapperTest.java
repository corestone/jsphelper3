package chk.jsphelper.module.mapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import chk.jsphelper.DataList;
import chk.jsphelper.ObjectLoader;
import chk.jsphelper.module.pool.ConnectionPoolManager;

public class DataMapperTest
{
	private static DataMapper dm = null;

	@Before
	public void setUp () throws Exception
	{
		ObjectLoader.isSuccess();
		Connection conn = ConnectionPoolManager.getInstance().getConnection("db", "Test");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM test");
		dm = new DataMapper(rs);
	}

	@Test
	public void testCreateDataList ()
	{
		try
		{
			DataList dl = dm.createDataList("UTF-8", "UTF-8");
			System.out.println(dl.toString());
			Map<String, String> m = new HashMap<String, String>();
			m.put("TEST_OUT", "OUT");
			dl = dm.setOutParameter(m, "UTF-8", "UTF-8");
			System.out.println(dl.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testSetOutParameter ()
	{
//		fail("Not yet implemented");
	}

	@Test
	public void testSetReturnParam ()
	{
//		fail("Not yet implemented");
	}

}
