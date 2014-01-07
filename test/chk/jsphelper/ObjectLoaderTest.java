package chk.jsphelper;

/**
 * Created with IntelliJ IDEA.
 * User: Corestone
 * Date: 13. 7. 17
 * Time: 오후 1:37
 * To change this template use File | Settings | File Templates.
 */

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectLoaderTest
{
	@Test
	public void testReloadXML ()
	{
		assertTrue(ObjectLoader.isSuccess());
		assertTrue(ObjectLoader.reloadXml());
	}
}
