package chk.jsphelper;

import static org.junit.Assert.fail;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import chk.jsphelper.object.DataSource;
import chk.jsphelper.object.Excel;
import chk.jsphelper.object.Mail;
import chk.jsphelper.object.Servlet;
import chk.jsphelper.object.Transaction;
import chk.jsphelper.object.enums.ObjectType;

public class ObjectFactoryTest
{

	@Test
	public void testGetDataSource ()
	{
		DataSource ds = ObjectFactory.getDataSource("db");
		Assert.assertEquals(ds.getId(), "db");
	}

	@Test
	public void testExistsID ()
	{
		Assert.assertTrue(ObjectFactory.existsID(ObjectType._DATASOURCE, "db"));
		Assert.assertTrue(ObjectFactory.existsID(ObjectType.EXCEL, "excel2DB"));
		Assert.assertTrue(ObjectFactory.existsID(ObjectType.MAIL, "passwd.find"));
//		Assert.assertTrue(ObjectFactory.existsID(ObjectType.MESSAGE, "passwd.find"));
		Assert.assertTrue(ObjectFactory.existsID(ObjectType.SERVLET, "main.index"));
		Assert.assertTrue(ObjectFactory.existsID(ObjectType.SQL, "test"));
		Assert.assertTrue(ObjectFactory.existsID(ObjectType.TRANSACTION, "product.modifyform"));
		Assert.assertTrue(ObjectFactory.existsID(ObjectType.UPLOAD, "recruit.file"));
	}

	@Test
	public void testGetExcel ()
	{
		Excel excel = ObjectFactory.getExcel("excel2DB");
		Assert.assertEquals(excel.getId(), "excel2DB");
	}

	@Test
	public void testGetObjectIDs ()
	{
		System.out.println("############ testGetObjectIDs ############");
		Iterator<String> ids = ObjectFactory.getObjectIDs(ObjectType.SQL);
		while (ids.hasNext())
		{
			System.out.println("[" + ids.next() + "]");
		}
	}

	@Test
	public void testGetMail ()
	{
		Mail mail = ObjectFactory.getMail("passwd.find");
		Assert.assertEquals(mail.getId(), "passwd.find");
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
		Assert.assertEquals(servlet.getId(), "main.index");
	}

	@Test
	public void testGetSql ()
	{
		Servlet servlet = ObjectFactory.getServlet("main.index");
		Assert.assertEquals(servlet.getId(), "main.index");
	}

	@Test
	public void testGetTransaction ()
	{
		Transaction transaction = ObjectFactory.getTransaction("product.modifyform");
		Assert.assertEquals(transaction.getId(), "product.modifyform");
	}

	@Test
	public void testGetObject ()
	{
		Transaction transaction = ObjectFactory.getTransaction("product.modifyform");
		Assert.assertEquals(transaction.getId(), "product.modifyform");
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
