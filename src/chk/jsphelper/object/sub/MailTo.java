package chk.jsphelper.object.sub;

public class MailTo
{
	private String bcc;
	private String cc;
	private String email;
	private String name;

	public String getBcc ()
	{
		return this.bcc;
	}

	public String getCc ()
	{
		return this.cc;
	}

	public String getEmail ()
	{
		return this.email;
	}

	public String getName ()
	{
		return this.name;
	}

	public void setBcc (final String bcc)
	{
		this.bcc = bcc;
	}

	public void setCc (final String cc)
	{
		this.cc = cc;
	}

	public void setEmail (final String email)
	{
		this.email = email;
	}

	public void setName (final String name)
	{
		this.name = name;
	}
}