package chk.jsphelper;

import static org.junit.Assert.*;

import org.apache.log4j.Level;
import org.junit.Before;
import org.junit.Test;

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
		Constant.getLogger().fatal("Fatal");
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

	@Test
	public void testSetLogLevel ()
	{
		Constant.setLogLevel(Level.INFO);
		Constant.getLogger().fatal("Fatal");
		Constant.getLogger().error("Error");
		Constant.getLogger().warn("Warn");
		Constant.getLogger().info("Info");
		Constant.getLogger().debug("Debug");
		Constant.getLogger().trace("Trace");
	}
}