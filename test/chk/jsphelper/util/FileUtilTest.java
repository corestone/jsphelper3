package chk.jsphelper.util;

import static org.junit.Assert.*;
import static chk.jsphelper.util.FileUtil.*;

import org.junit.Test;

public class FileUtilTest
{
	@Test
	public void testPath2FileName ()
	{
		assertNull(path2FileName(null));
		assertEquals("", path2FileName(""));
		assertEquals("file.ext", path2FileName("/home/www/webapps/file.ext"));
		assertEquals("folder", path2FileName("/home/www/webapps/directory/folder"));
	}

}