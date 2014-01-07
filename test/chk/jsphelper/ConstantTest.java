package chk.jsphelper;

/**
 * Created with IntelliJ IDEA.
 * User: Corestone
 * Date: 13. 7. 17
 * Time: 오후 1:31
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantTest
{
	@Before
	public void setUp () throws Exception
	{
		System.out.println(Constant.getVersion());
	}

	@Test
	public void testGetLogger ()
	{
		Constant.getLogger().error("Error");
		Constant.getLogger().warn("Warn");
		Constant.getLogger().info("Info");
		Constant.getLogger().debug("Debug");
		Constant.getLogger().trace("Trace");
	}

	@Test
	public void testGetValueStringString ()
	{
		assertEquals(Constant.getValue("Path.Client", "/"), "/jsphelper/");
		assertEquals(Constant.getValue("Path.Cli", "/jsphe"), "/jsphe");
	}

	@Test
	public void testGetValueStringStringInt ()
	{
		assertEquals(Constant.getValue("Value.LinePerPage", "10", 0), "20");
		assertEquals(Constant.getValue("Value.LinePerPage", "5", 2), "5");
	}
}
