package chk.jsphelper.value.setter;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.value.ExcelValue;

import java.util.Map;

public class ExcelValueSetter extends ExcelValue implements ValueSetter
{
	public ExcelValueSetter (final String functionID)
	{
		super(functionID);
	}

	public ExcelValue getValue ()
	{
		try
		{
			return (ExcelValue) this.clone();
		}
		catch (final CloneNotSupportedException cnse)
		{
			Constant.getLogger().error("ExcelValueSetter.getValue에서 에러 발생 {}", new Object[] {cnse.getMessage()}, cnse);
		}
		return null;
	}

	public final void setSuccess (final boolean success)
	{
		this.success = success;
	}

	public void setValueObject (final Map<String, Object> hm)
	{
		this.applyCount = (Integer) hm.get("applyCount");
		this.dl = (DataList) hm.get("DataList");
	}
}