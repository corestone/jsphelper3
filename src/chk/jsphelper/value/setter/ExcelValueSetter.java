package chk.jsphelper.value.setter;

import java.util.Map;

import chk.jsphelper.DataList;
import chk.jsphelper.value.ExcelValue;

public class ExcelValueSetter extends ExcelValue implements ValueSetter
{
	public ExcelValueSetter (final String functionID)
	{
		super(functionID);
	}

	public ExcelValue getValue () throws CloneNotSupportedException
	{
		try
		{
			return (ExcelValue) this.clone();
		}
		catch (final CloneNotSupportedException cnse)
		{
			throw cnse;
		}
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