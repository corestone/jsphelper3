package chk.jsphelper.service.tag;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ComboBox extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3766951368141451958L;
	private String attribute = "";
	private DataList dl = null;
	private boolean group = false;
	private String name;
	private String onchange = "";
	private String title = "";
	private String value = "";

	@Override
	public int doStartTag () throws JspException
	{
		final StringBuilder sb1 = new StringBuilder("<select");
		sb1.append (" id=\"").append (StringUtil.trimDefault (this.id, this.name)).append ("\" name=\"").append (this.name).append ("\"");
		if (this.onchange.length() > 0)
		{
			sb1.append (" onchange=\"").append (this.onchange).append ("\"");
		}
		if (this.attribute.length() > 0)
		{
			sb1.append (" ").append (this.attribute);
		}
		sb1.append(">");
		if (!this.title.equalsIgnoreCase("remove"))
		{
			sb1.append ("<option value=\"\">").append (this.title).append ("</option>");
		}
		if (this.dl == null)
		{
			Constant.getLogger().error("RecordSet이 null이라서 데이타를 받아오지 못해 에러가 발생하였습니다.");
			this.dl = new DataList(ArrayUtils.EMPTY_STRING_ARRAY);
		}
		final StringBuilder sb2 = new StringBuilder("");
		final String[] values = this.dl.getFieldData(1);
		final String[] texts = this.dl.getFieldData(2);
		final String[] groups = this.dl.getFieldData(3);
		if (values.length > 0)
		{
			sb2.append ("<option value=\"").append (values[0]).append ("\"").append (values[0].equals (this.value) ? " selected=\"selected\"" : "").append (">").append (texts[0]).append ("</option>");
		}
		for (int i = 1, z = values.length; i < z; i++)
		{
			if (this.group && !groups[i].equals(groups[i - 1]))
			{
				sb2.append ("</optgroup><optgroup label=\"").append (groups[i - 1]).append ("\">");
			}
			sb2.append ("<option value=\"").append (values[i]).append ("\"").append (values[i].equals (this.value) ? " selected=\"selected\"" : "").append (">").append (texts[i]).append ("</option>");
		}
		final JspWriter out = this.pageContext.getOut();
		try
		{
			if (this.group)
			{
				out.print(sb1.toString() + (sb2.length() < 12 ? sb2.toString() : (sb2.substring(11) + "</optgroup>")) + "</select>");
			}
			else
			{
				out.print(sb1.toString() + sb2.toString() + "</select>");
			}
		}
		catch (final IOException ioe)
		{
			Constant.getLogger().error(ioe.getLocalizedMessage(), ioe);
		}
		return Tag.SKIP_BODY;
	}

	/**
	 * @param attribute
	 */
	public void setAttribute (final String attribute)
	{
		this.attribute = attribute;
	}

	/**
	 * @param dl
	 */
	public void setData (final DataList dl)
	{
		this.dl = dl;
	}

	/**
	 * @param group
	 */
	public void setGroup (final boolean group)
	{
		this.group = group;
	}

	/**
	 * @param id
	 */
	@Override
	public void setId (final String id)
	{
		this.id = id;
	}

	/**
	 * @param name
	 */
	public void setName (final String name)
	{
		this.name = name;
	}

	/**
	 * @param onchange
	 */
	public void setOnchange (final String onchange)
	{
		this.onchange = onchange;
	}

	/**
	 * @param title
	 */
	public void setTitle (final String title)
	{
		this.title = title;
	}

	/**
	 * @param value
	 */
	public void setValue (final String value)
	{
		this.value = value;
	}
}