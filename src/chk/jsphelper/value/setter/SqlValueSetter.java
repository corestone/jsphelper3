package chk.jsphelper.value.setter;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.value.SqlValue;

import java.util.Map;

public class SqlValueSetter extends SqlValue implements ValueSetter
{
	public SqlValueSetter (final String functionID)
	{
		super(functionID);
	}

	public SqlValue getValue ()
	{
		try
		{
			return (SqlValue) this.clone();
		}
		catch (final CloneNotSupportedException cnse)
		{
			Constant.getLogger().error("SqlValueSetter.getValue에서 에러 발생 {}", new Object[] {cnse.getMessage()}, cnse);
		}
		return null;
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