package chk.jsphelper.value.setter;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.value.TransactionValue;

import java.util.Map;

public class TransactionValueSetter extends TransactionValue implements ValueSetter
{
	public TransactionValueSetter (final String functionID, final int size)
	{
		super(functionID, size);
	}

	public TransactionValue getValue ()
	{
		try
		{
			return (TransactionValue) this.clone();
		}
		catch (final CloneNotSupportedException cnse)
		{
			Constant.getLogger().error("TransactionValueSetter.getValue에서 에러 발생 {}", new Object[] {cnse.getMessage()}, cnse);
		}
		return null;
	}

	public final void setSuccess (final boolean success)
	{
		this.success = success;
	}

	public void setValueObject (final Map<String, Object> hm)
	{
		this.dl = (DataList[]) hm.get("DataList");
		this.totalSize = (int[]) hm.get("pagingTotalSize");
		this.updateCount = (int[]) hm.get("updateCount");
	}
}