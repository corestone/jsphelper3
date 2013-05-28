package chk.jsphelper.engine.ext.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import chk.jsphelper.Constant;
import chk.jsphelper.module.runnable.AbstractRunnable;

/**
 * @author Corestone
 */
public class MailSender extends AbstractRunnable
{
	private final String body;
	private final String charSet;
	private final String mailbcc;
	private final String mailcc;
	private final Session mailsession;
	private final String mailto;
	private final String subject;
	private final String type;

	/**
	 * @param mailsession
	 * @param mailto
	 * @param mailcc
	 * @param mailbcc
	 * @param subject
	 * @param body
	 * @param type
	 * @param charSet
	 */
	public MailSender (final Session mailsession, final String mailto, final String mailcc, final String mailbcc, final String subject, final String body, final String type, final String charSet)
	{
		super(0);
		this.mailsession = mailsession;
		this.mailto = mailto;
		this.mailcc = mailcc;
		this.mailbcc = mailbcc;
		this.subject = subject;
		this.body = body;
		this.type = type;
		this.charSet = charSet;
	}

	@Override
	public void run ()
	{
		try
		{
			this.send();
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void send () throws Exception
	{
		try
		{
			String mailSubject = null;
			String mailBody = null;
			mailSubject = this.subject;
			mailBody = this.body;
			Constant.getLogger().debug("MAIL TO : {}", this.mailto);
			Constant.getLogger().debug("MAIL CC : {}", this.mailcc);
			Constant.getLogger().debug("MAIL BCC : {}", this.mailbcc);
			Constant.getLogger().debug("MAIL SUBJECT : {}", mailSubject);
			Constant.getLogger().debug("MAIL BODY :\n{}", mailBody);

			final Message msg = new MimeMessage(this.mailsession);

			msg.setFrom(new InternetAddress(this.mailsession.getProperty("mailfrom"), this.mailsession.getProperty("denote")));

			msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(this.mailto, false));

			if (!this.mailcc.equals(""))
			{
				msg.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(this.mailcc, false));
			}

			if (!this.mailbcc.equals(""))
			{
				msg.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(this.mailbcc, false));
			}

			msg.setSubject(mailSubject);
			final MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(mailBody, "text/" + this.type + "; charset=" + this.charSet);
			final Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mbp);
			msg.setContent(multipart);
			msg.setSentDate(new Date());
			Transport.send(msg);
			Constant.getLogger().info("MAIL 메일 발송 완료");
		}
		catch (final Exception e)
		{
			Constant.getLogger().error("메일을 발송하는데 {} 예외가 발생하였습니다.", e.getClass().getName());
			throw e;
		}
	}
}