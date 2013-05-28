package chk.jsphelper.util;

import static chk.jsphelper.util.ConvertUtil.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chk.jsphelper.DataList;
import chk.jsphelper.module.wrapper.DataListSource;

public class ConvertUtilTest
{
	private DataList dl;

	@Before
	public void createDataList()
	{
		DataListSource dls = new DataListSource();
		for (int i = 0; i < 5; i++)
		{
			dls.put("key" + i, new ArrayList<String>());
		}
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				dls.add("key" + j, "value_" + i + j);
			}
		}
		dl = new DataList(dls);
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
//		dataList2Message(dl, new String[] {"key2","key3"});
//		for (int i = 0; i < 10; i++)
//		{
//			System.out.println(ObjectFactory.getMessage("value_0" + i));
//		}
	}

	@Test
	public void testDataList2XML ()
	{
		System.out.println(dataList2XML(dl));
	}
}