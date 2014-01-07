package chk.jsphelper.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;
import chk.jsphelper.engine.ext.mail.MailAuthenticator;
import chk.jsphelper.engine.ext.mail.MailSender;
import chk.jsphelper.module.mapper.ParameterMapper;
import chk.jsphelper.module.pool.ThreadPool;
import chk.jsphelper.object.Mail;
import chk.jsphelper.util.StringUtil;

/**
 * @author Corestone
 */
public class MailEngine implements InterfaceEngine
{
	private Mail mail = null;

	private Session mailSession = null;
	private Parameter param = null;
	private boolean resultValue;

	/**
	 * @param object
	 * @param param
	 */
	public MailEngine (final Mail object, final Parameter param)
	{
		this.mail = object;
		this.param = param;
	}

	public void execute () throws Exception
	{
		this.mailSession = this.getMailSession();
		Constant.getLogger().debug("MAIL TO TEMPLATE : {}", this.mail.getToEmail());
		Constant.getLogger().debug("MAIL CC TEMPLATE : {}", this.mail.getToCC());
		Constant.getLogger().debug("MAIL BCC TEMPLATE : {}", this.mail.getToBCC());
		Constant.getLogger().debug("MAIL SUBJECT TEMPLATE : {}", this.mail.getSubject());
		Constant.getLogger().debug("MAIL BODY TEMPLATE :\n{}", this.mail.getBody());

		final ParameterMapper pm = new ParameterMapper(this.param);
		pm.checkParam();
		final String[] mailto = pm.convertMappingTexts(this.mail.getToEmail());
		final String[] mailcc = pm.convertMappingTexts(StringUtil.trim(this.mail.getToCC()));
		final String[] mailbcc = pm.convertMappingTexts(StringUtil.trim(this.mail.getToBCC()));
		final String[] subject = pm.convertMappingTexts(this.mail.getSubject());
		final String[] body = pm.convertMappingTexts(this.mail.getBody());
		this.resultValue = this.mail.isThread() ? false : true;

		for (int i = 0; i < mailto.length; i++)
		{
			// 혹시 매핑된 값이 하나인 경우가 있을 경우에 대한 대비
			final String cc = (mailcc.length == 1) ? mailcc[0] : mailcc[i];
			final String bcc = (mailbcc.length == 1) ? mailbcc[0] : mailbcc[i];
			final String title = (subject.length == 1) ? subject[0] : subject[i];
			final String contents = (body.length == 1) ? body[0] : body[i];
			final MailSender ms = new MailSender(this.mailSession, mailto[i], cc, bcc, title, contents, this.mail.getType(), this.mail.getCharset());
			if (this.mail.isThread())
			{
				ThreadPool.getInstance().assign(ms);
			}
			else
			{
				ms.send();
			}
		}
		this.resultValue = true;
	}

	public Map<String, Object> getValueObject ()
	{
		final Map<String, Object> hm = new HashMap<String, Object>();
		hm.put("RESULT", this.resultValue);
		return hm;
	}

	private Session getMailSession ()
	{
		Constant.getLogger().info("smtp host : {}", this.mail.getHost());
		Constant.getLogger().info("mailfrom : {}", this.mail.getFromEmail());
		Constant.getLogger().info("user : {}", this.mail.getFromUserid());
		Constant.getLogger().info("password : {}", this.mail.getFromPasswd());
		Constant.getLogger().info("denote : {}", this.mail.getFromName());

		if (this.mail.getHost() == null)
		{
			Constant.getLogger().error("메일을 보낼 SMTP 서버가 설정이 되지 않았습니다.");
		}
		if (this.mail.getFromUserid() == null)
		{
			Constant.getLogger().error("메일을 보낼 SMTP 서버 계정 유저가 설정이 되지 않았습니다.");
		}
		if (this.mail.getFromPasswd() == null)
		{
			Constant.getLogger().error("메일을 보낼 SMTP 서버 계정 패스워드가 설정이 되지 않았습니다.");
		}
		if (this.mail.getFromEmail() == null)
		{
			Constant.getLogger().error("메일을 보낼 E-Mail 주소가 설정이 되지 않았습니다.");
		}
		final Properties prop = System.getProperties();
		final MailAuthenticator authen = new MailAuthenticator(this.mail.getFromUserid(), this.mail.getFromPasswd());
		if (this.mail.getFromPasswd() != null)
		{
			prop.put("mail.smtp.auth", "true");
		}
		prop.put("mail.smtp.host", this.mail.getHost());
		prop.put("mailfrom", this.mail.getFromEmail());
		if (this.mail.getFromName() != null)
		{
			prop.put("denote", this.mail.getFromName());
		}
		final Session nameSession = Session.getInstance(prop, authen);
		if (this.mail.isDebug())
		{
			nameSession.setDebug(true);
		}
		else
		{
			nameSession.setDebug(false);
		}
		return nameSession;
	}
}