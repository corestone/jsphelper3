package chk.jsphelper.value.setter;

import java.util.Map;

import chk.jsphelper.DataList;
import chk.jsphelper.value.SqlValue;

public class SqlValueSetter extends SqlValue implements ValueSetter
{
	public SqlValueSetter (final String functionID)
	{
		super(functionID);
	}

	public SqlValue getValue () throws CloneNotSupportedException
	{
		try
		{
			return (SqlValue) this.clone();
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
		this.dl = (DataList) hm.get("DataList");
		this.totalSize = (Integer) hm.get("pagingTotalSize");
		this.updateCount = (Integer) hm.get("updateCount");
	}
}