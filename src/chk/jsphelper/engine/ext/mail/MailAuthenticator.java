package chk.jsphelper.engine.ext.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author Corestone
 */
public class MailAuthenticator extends Authenticator
{
	private final String m_id;
	private final String m_password;

	/**
	 * @param id
	 * @param password
	 */
	public MailAuthenticator (final String id, final String password)
	{
		this.m_id = id;
		this.m_password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication ()
	{
		return new PasswordAuthentication(this.m_id, this.m_password);
	}
}