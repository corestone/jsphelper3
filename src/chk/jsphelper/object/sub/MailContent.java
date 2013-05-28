package chk.jsphelper.object.sub;

public class MailContent
{
	private String subject;
	private String type;

	public String getSubject ()
	{
		return this.subject;
	}

	public String getType ()
	{
		return this.type;
	}

	public void setSubject (final String subject)
	{
		this.subject = subject;
	}

	public void setType (final String type)
	{
		this.type = type;
	}
}