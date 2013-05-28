package chk.jsphelper.value.setter;

import java.util.Map;

import chk.jsphelper.value.AbstractValue;

public interface ValueSetter
{
	public AbstractValue getValue () throws CloneNotSupportedException;

	public void setSuccess (boolean success);

	public void setValueObject (final Map<String, Object> hm);
}