package chk.jsphelper.value.setter;

import chk.jsphelper.Constant;
import chk.jsphelper.value.UploadValue;

import java.util.List;
import java.util.Map;

public class UploadValueSetter extends UploadValue implements ValueSetter
{
	public UploadValueSetter (final String functionID)
	{
		super(functionID);
	}

	public UploadValue getValue ()
	{
		try
		{
			return (UploadValue) this.clone();
		}
		catch (final CloneNotSupportedException cnse)
		{
			Constant.getLogger().error("UploadValueSetter.getValue에서 에러 발생 {}", new Object[] {cnse.getMessage()}, cnse);
		}
		return null;
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