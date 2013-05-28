package chk.jsphelper.service.controller;

import chk.jsphelper.Constant;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.Parameter;
import chk.jsphelper.engine.ExcelEngine;
import chk.jsphelper.engine.InterfaceEngine;
import chk.jsphelper.object.Excel;
import chk.jsphelper.util.DateUtil;
import chk.jsphelper.value.ExcelValue;
import chk.jsphelper.value.setter.ExcelValueSetter;
import chk.jsphelper.value.setter.ValueSetter;

public class ExcelController
{
	public ExcelValue executeExcel (final String objectID, final Parameter param)
	{
		final long stime = System.nanoTime();
		Constant.getLogger().debug("executeExcel id:{} 시작", objectID);
		final ValueSetter evs = new ExcelValueSetter(objectID);
		try
		{
			final Excel excel = ObjectFactory.getExcel(objectID);
			final InterfaceEngine ie = new ExcelEngine(excel, param);
			ie.execute();
			evs.setValueObject(ie.getValueObject());
			evs.setSuccess(true);
		}
		catch (final Exception e)
		{
			evs.setSuccess(false);
			Constant.getLogger().error("엑셀 변환작업 중에 에러가 발생하였습니다.", e);
		}
		finally
		{
			Constant.getLogger().debug("executeExcel id:{} 마침 - {}", new String[] { objectID, DateUtil.getExecutedTime(stime) });
		}
		return (ExcelValue) evs;
	}
}