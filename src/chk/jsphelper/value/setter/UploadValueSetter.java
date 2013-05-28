package chk.jsphelper.value.setter;

import java.util.List;
import java.util.Map;

import chk.jsphelper.value.UploadValue;

public class UploadValueSetter extends UploadValue implements ValueSetter
{
	public UploadValueSetter (final String functionID)
	{
		super(functionID);
	}

	public UploadValue getValue () throws CloneNotSupportedException
	{
		try
		{
			return (UploadValue) this.clone();
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
		for (int i = 0, z = ((List<?>) hm.get("NAME")).size(); i < z; i++)
		{
			this.fileInfo.get("NAME").add((String) ((List<?>) hm.get("NAME")).get(i));
			this.fileInfo.get("TYPE").add((String) ((List<?>) hm.get("TYPE")).get(i));
			this.fileInfo.get("OLD").add((String) ((List<?>) hm.get("OLD")).get(i));
			this.fileInfo.get("NEW").add((String) ((List<?>) hm.get("NEW")).get(i));
			this.fileInfo.get("SIZE").add((String) ((List<?>) hm.get("SIZE")).get(i));
		}
		this.uploadPath = (String) hm.get("DIR");
	}
}