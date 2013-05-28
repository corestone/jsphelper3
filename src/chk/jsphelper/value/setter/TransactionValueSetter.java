package chk.jsphelper.value.setter;

import java.util.Map;

import chk.jsphelper.DataList;
import chk.jsphelper.value.TransactionValue;

public class TransactionValueSetter extends TransactionValue implements ValueSetter
{
	public TransactionValueSetter (final String functionID, final int size)
	{
		super(functionID, size);
	}

	public TransactionValue getValue () throws CloneNotSupportedException
	{
		try
		{
			return (TransactionValue) this.clone();
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
		this.dl = (DataList[]) hm.get("DataList");
		this.totalSize = (int[]) hm.get("pagingTotalSize");
		this.updateCount = (int[]) hm.get("updateCount");
	}
}