package chk.jsphelper.object;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.object.enums.ObjectType;

public class Upload implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType.UPLOAD;
	protected String accept;
	protected String deny;
	protected String description;
	protected String dir;
	protected String id;
	protected String limit;

	public String getAccept ()
	{
		return this.accept;
	}

	public String getDeny ()
	{
		return this.deny;
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
	 * 업로드되는 절대폴더를 가지고 오는 메소드
	 * 
	 * @return - 업로드되는 서버의 절대경로
	 */
	public String getDir ()
	{
		return this.dir;
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
	 * 업로드 하는 파일의 크기제한을 가지고 오는 메소드
	 * 
	 * @return - 파일의 크기제한(단위 포함)
	 */
	public String getLimit ()
	{
		return this.limit;
	}

	public ObjectType getObjectType ()
	{
		return Upload.TYPE;
	}

	public void setAccept (final String appect)
	{
		this.accept = appect;
	}

	public void setDeny (final String deny)
	{
		this.deny = deny;
	}

	public void setDescription (final String description)
	{
		this.description = description;
	}

	/**
	 * @param saveDir
	 */
	public void setDir (String dir)
	{
		if (!dir.endsWith("/"))
		{
			dir += "/";
		}
		this.dir = dir;
	}

	public void setId (final String id)
	{
		this.id = id;
	}

	/**
	 * @param listSize
	 */
	public void setLimit (final String limit)
	{
		this.limit = limit;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}