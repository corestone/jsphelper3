package chk.jsphelper;

import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class ObjectLoaderTest
{
	@Test
	public void testReloadXML()
	{
		assertTrue(ObjectLoader.isSuccess());
		assertTrue(ObjectLoader.reloadXml());
	}
}