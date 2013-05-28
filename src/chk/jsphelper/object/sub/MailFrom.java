package chk.jsphelper.object.sub;

public class MailFrom
{
	private String denote;
	private String email;
	private String passwd;
	private String userid;

	public String getDenote ()
	{
		return this.denote;
	}

	public String getEmail ()
	{
		return this.email;
	}

	public String getPasswd ()
	{
		return this.passwd;
	}

	public String getUserid ()
	{
		return this.userid;
	}

	public void setDenote (final String denote)
	{
		this.denote = denote;
	}

	public void setEmail (final String email)
	{
		this.email = email;
	}

	public void setPasswd (final String passwd)
	{
		this.passwd = passwd;
	}

	public void setUserid (final String userid)
	{
		this.userid = userid;
	}
}