package chk.jsphelper.service.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;

public class PageCombo extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1163128651147236958L;
	private int configIndex = 0;
	private Parameter param;
	private String title = "";
	private int total;

	@Override
	public int doStartTag () throws JspException
	{
		final StringBuilder sb = new StringBuilder("");
		this.param.setPaging(this.configIndex);
		final int index = this.param.getCurrentIndex();
		final String indexCurrentParam = Constant.getValue("KeyName.CurrentIndex", "_currentIndex", this.configIndex);

		if (this.total > 0)
		{
			sb.append("<select id=\"" + indexCurrentParam + "\" name=\"" + indexCurrentParam + "\" onchange=\"_goPage('" + this.configIndex + "');\">");
			sb.append("<option value=\"\">" + this.title + "</option>");
			for (int i = 0; i < this.total; i++)
			{
				if (i == (index - 1))
				{
					sb.append("<option value=\"" + (i + 1) + "\" selected=\"selected\">" + (i + 1) + "</option>");
				}
				else
				{
					sb.append("<option value=\"" + (i + 1) + "\">" + (i + 1) + "</option>");
				}
			}
			sb.append("</select>");
		}

		final JspWriter out = this.pageContext.getOut();
		try
		{
			out.print(sb.toString());
		}
		catch (final IOException ioe)
		{
		}
		return Tag.SKIP_BODY;
	}

	/**
	 * @param configIndex
	 */
	public void setConfigIndex (final int configIndex)
	{
		this.configIndex = configIndex;
	}

	/**
	 * @param param
	 */
	public void setParam (final Parameter param)
	{
		this.param = param;
	}

	public void setTitle (final String title)
	{
		this.title = title;
	}

	/**
	 * @param total
	 */
	public void setTotal (final int total)
	{
		this.total = total;
	}
}