package chk.jsphelper.util;

import static chk.jsphelper.util.ConvertUtil.*;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import chk.jsphelper.DataList;

public class ConvertUtilTest
{
	private DataList dl;

	@Before
	public void createDataList ()
	{
		String[] keys = new String[5];
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = "field_" + Integer.toString(i + 1);
		}
		dl = new DataList(keys);

		for (int i = 0; i < 10; i++)
		{
			Map<String, String> data = new HashMap<String, String>();
			for (int j = 0; j < 5; j++)
			{
				data.put("field_" + Integer.toString(i + 1), "row:" + i + ",column:" + j);
			}
			dl.addData(data);
		}
	}

	@Test
	public void testDataList2JSON ()
	{
		System.out.println(dl);
		System.out.println(dataList2JSON(dl));

		assertTrue(true);
	}

	@Test
	public void testDataList2Map ()
	{
		System.out.println(dataList2Map(dl));
	}

	@Test
	public void testDataList2Message ()
	{
// dataList2Message(dl, new String[] {"key2","key3"});
// for (int i = 0; i < 10; i++)
// {
// System.out.println(ObjectFactory.getMessage("value_0" + i));
// }
	}

	@Test
	public void testDataList2XML ()
	{
		System.out.println(dataList2XML(dl));
	}
}
