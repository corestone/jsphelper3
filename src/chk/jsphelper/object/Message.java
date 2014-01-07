package chk.jsphelper.object;

import chk.jsphelper.object.enums.ObjectType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

public class Message implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType.MESSAGE;
	private String description;
	private String id;
	private final Map<String, String> message = new HashMap<String, String>();

	public void addString (final String langCoce, final String langText)
	{
		this.message.put(langCoce, langText);
	}

	/**
	 * @return
	 */
	public boolean existsLang (final String lang)
	{
		return this.message.containsKey(lang);
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
	 * 오브젝트의 id를 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 id
	 */
	public final String getId ()
	{
		return this.id;
	}

	/**
	 * @return
	 */
	public Map<String, String> getMessage ()
	{
		return this.message;
	}

	public ObjectType getObjectType ()
	{
		return Message.TYPE;
	}

	public String getText (final String lang)
	{
		return this.message.get(lang);
	}

	/**
	 * @param description
	 */
	public void setDescription (final String description)
	{
		this.description = description;
	}

	public void setId (final String id)
	{
		this.id = id;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}