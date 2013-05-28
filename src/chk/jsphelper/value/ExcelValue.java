package chk.jsphelper.value;

import javax.servlet.http.HttpServletRequest;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.object.Excel;

public class ExcelValue extends AbstractValue
{
	protected int applyCount = 0;
	protected DataList dl = null;

	public ExcelValue (final String functionID)
	{
		super(functionID);
	}

	/**
	 * 프로세스에 의해 반영된 레코드 수
	 * 
	 * @return - 반영된 레코드 수(-1는 실패를 의미한다)
	 */
	public int getApplyCount ()
	{
		return this.applyCount;
	}

	/**
	 * 엑셀을 레코드셋으로 가지고 오는 프로세스일 경우 엑셀의 데이타를 가지고 있는 레코드셋을 리턴하는 메소드이다.
	 * 
	 * @return - 엑셀의 데이타를 가지고 있는 레코드
	 */
	public DataList getDataList ()
	{
		return this.dl;
	}

	@Override
	public Excel getObject ()
	{
		return ObjectFactory.getExcel(this.functionID);
	}

	@Override
	public void loggingObject ()
	{
		Constant.getLogger().info("Excel ID : " + this.functionID + "에 쓰인 값들\n" + ObjectFactory.getExcel(this.functionID));
	}

	@Override
	public void setRequest (final HttpServletRequest req)
	{
		req.setAttribute("excel." + this.functionID + ".applyCount", this.applyCount);
		req.setAttribute("excel." + this.functionID + ".dataList", this.dl);
	}
}