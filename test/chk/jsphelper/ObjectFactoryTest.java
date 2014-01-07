package chk.jsphelper;

/**
 * Created with IntelliJ IDEA.
 * User: Corestone
 * Date: 13. 7. 17
 * Time: 오후 1:36
 * To change this template use File | Settings | File Templates.
 */

import chk.jsphelper.object.*;
import chk.jsphelper.object.enums.ObjectType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectFactoryTest
{
	@Test
	public void testGetDataSource ()
	{
		DataSource ds = ObjectFactory.getDataSource("db");
		assertEquals(ds.getId(), "db");
	}

	@Test
	public void testExistsID ()
	{
		assertTrue(ObjectFactory.existsID(ObjectType._DATASOURCE, "db"));
		assertTrue(ObjectFactory.existsID(ObjectType.EXCEL, "excel2DB"));
		assertTrue(ObjectFactory.existsID(ObjectType.MAIL, "passwd.find"));
// Assert.assertTrue(ObjectFactory.existsID(ObjectType.MESSAGE, "passwd.find"));
		assertTrue(ObjectFactory.existsID(ObjectType.SERVLET, "main.index"));
		assertTrue(ObjectFactory.existsID(ObjectType.SQL, "test"));
		assertTrue(ObjectFactory.existsID(ObjectType.TRANSACTION, "product.modifyform"));
		assertTrue(ObjectFactory.existsID(ObjectType.UPLOAD, "recruit.file"));
	}

	@Test
	public void testGetExcel ()
	{
		Excel excel = ObjectFactory.getExcel("excel2DB");
		assertEquals(excel.getId(), "excel2DB");
	}

	@Test
	public void testGetObjectIDs ()
	{
		System.out.println("############ testGetObjectIDs ############");
		for (String id : ObjectFactory.getObjectIDs(ObjectType.SQL))
		{
			System.out.println("[" + id + "]");
		}
	}

	@Test
	public void testGetMail ()
	{
		Mail mail = ObjectFactory.getMail("passwd.find");
		assertEquals(mail.getId(), "passwd.find");
	}

	@Test
	public void testGetMessage ()
	{
//
	}

	@Test
	public void testGetServlet ()
	{
		Servlet servlet = ObjectFactory.getServlet("main.index");
		assertEquals(servlet.getId(), "main.index");
	}

	@Test
	public void testGetSql ()
	{
		Servlet servlet = ObjectFactory.getServlet("main.index");
		assertEquals(servlet.getId(), "main.index");
	}

	@Test
	public void testGetTransaction ()
	{
		Transaction transaction = ObjectFactory.getTransaction("product.modifyform");
		assertEquals(transaction.getId(), "product.modifyform");
	}

	@Test
	public void testGetObject ()
	{
		Transaction transaction = ObjectFactory.getTransaction("product.modifyform");
		assertEquals(transaction.getId(), "product.modifyform");
	}

	@Test
	public void testGetUpload ()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSize ()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testClearObject ()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPrintDump ()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPrintIDs ()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPutObject ()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveObject ()
	{
		fail("Not yet implemented");
	}
}
