package chk.jsphelper.object;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.object.sub.MailContent;
import chk.jsphelper.object.sub.MailFrom;
import chk.jsphelper.object.sub.MailTo;
import chk.jsphelper.util.StringUtil;

public class Mail implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType.MAIL;
	private String body;
	private String charset = "UTF-8";
	private boolean debug = false;
	private String description;
	private String fromEmail;
	private String fromName;
	private String fromPasswd;
	private String fromUserid;
	private String host;
	private String id;
	private String subject;
	private boolean thread = true;
	private String toBCC;
	private String toCC;
	private String toEmail;
	private String toName;
	private String type;

	/**
	 * @return
	 */
	public String getBody ()
	{
		return this.body;
	}

	/**
	 * 
	 */
	public String getCharset ()
	{
		return this.charset;
	}

	/**
	 * 오브젝트의 설명을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 설명인 description
	 */
	public final String getDescription ()
	{
		return this.description;
	}

	/**
	 * @return
	 */
	public String getFromEmail ()
	{
		return this.fromEmail;
	}

	/**
	 * @return
	 */
	public String getFromName ()
	{
		return this.fromName;
	}

	/**
	 * @return
	 */
	public String getFromPasswd ()
	{
		return this.fromPasswd;
	}

	/**
	 * @return
	 */
	public String getFromUserid ()
	{
		return this.fromUserid;
	}

	/**
	 * @return
	 */
	public String getHost ()
	{
		return this.host;
	}

	/**
	 * 오브젝트의 id를 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 id
	 */
	public final String getId ()
	{
		return this.id;
	}

	public ObjectType getObjectType ()
	{
		return Mail.TYPE;
	}

	/**
	 * @return
	 */
	public String getSubject ()
	{
		return this.subject;
	}

	/**
	 * @return
	 */
	public String getToBCC ()
	{
		return this.toBCC;
	}

	/**
	 * @return
	 */
	public String getToCC ()
	{
		return this.toCC;
	}

	/**
	 * @return
	 */
	public String getToEmail ()
	{
		return this.toEmail;
	}

	/**
	 * @return
	 */
	public String getToName ()
	{
		return this.toName;
	}

	/**
	 * @return
	 */
	public String getType ()
	{
		return this.type;
	}

	/**
	 * @return
	 */
	public boolean isDebug ()
	{
		return this.debug;
	}

	/**
	 * @return
	 */
	public boolean isThread ()
	{
		return this.thread;
	}

	public void putContent (final MailContent content)
	{
		this.subject = content.getSubject();
		this.type = content.getType();
	}

	public void putFrom (final MailFrom from)
	{
		this.fromEmail = from.getEmail();
		this.fromName = from.getDenote();
		this.fromPasswd = from.getPasswd();
		this.fromUserid = from.getUserid();
	}

	public void putTo (final MailTo to)
	{
		this.toBCC = to.getBcc();
		this.toCC = to.getCc();
		this.toEmail = to.getEmail();
		this.toName = to.getName();
	}

	public void setBody (final String body)
	{
		this.body = (this.type.equals("HTML")) ? StringUtil.compressWhitespace(body) : body;
	}

	/**
	 * @param charset
	 */
	public void setCharset (final String charset)
	{
		this.charset = charset;
	}

	/**
	 * @param debug
	 */
	public void setDebug (final boolean debug)
	{
		this.debug = debug;
	}

	public void setDescription (final String description)
	{
		this.description = description;
	}

	public void setHost (final String host)
	{
		this.host = host;
	}

	public void setId (final String id)
	{
		this.id = id;
	}

	/**
	 * @param thread
	 */
	public void setThread (final boolean thread)
	{
		this.thread = thread;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}