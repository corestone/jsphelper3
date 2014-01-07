package chk.jsphelper.object;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.object.enums.ObjectType;

public class Servlet implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType.SERVLET;
	protected boolean after = false;
	protected boolean before = false;
	protected String classname;
	protected String description;
	protected String id;
	protected String method;
	protected String upload;

	/**
	 * @return
	 */
	public String getClassname ()
	{
		return this.classname;
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
	public String getMethod ()
	{
		return this.method;
	}

	public ObjectType getObjectType ()
	{
		return Servlet.TYPE;
	}

	/**
	 * @return
	 */
	public String getUpload ()
	{
		return this.upload;
	}

	/**
	 * @return
	 */
	public boolean isAfter ()
	{
		return this.after;
	}

	/**
	 * @return
	 */
	public boolean isBefore ()
	{
		return this.before;
	}

	/**
	 * @param runAfter
	 */
	public void setAfter (final boolean runAfter)
	{
		this.after = runAfter;
	}

	/**
	 * @param runBefore
	 */
	public void setBefore (final boolean runBefore)
	{
		this.before = runBefore;
	}

	/**
	 * @param targetClass
	 */
	public void setClassname (final String targetClass)
	{
		this.classname = targetClass;
	}

	public void setDescription (final String description)
	{
		this.description = description;
	}

	public void setId (final String id)
	{
		this.id = id;
	}

	/**
	 * @param method
	 */
	public void setMethod (final String method)
	{
		this.method = method;
	}

	/**
	 * @param uploadID
	 */
	public void setUpload (final String uploadID)
	{
		this.upload = uploadID;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}