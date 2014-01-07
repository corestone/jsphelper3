package chk.jsphelper.service.tag;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

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
			sb.append("<select id=\"").append(indexCurrentParam).append("\" name=\"").append(indexCurrentParam).append("\" onchange=\"_goPage('").append(this.configIndex).append("');\">");
			sb.append("<option value=\"\">").append(this.title).append("</option>");
			for (int i = 0; i < this.total; i++)
			{
				if (i == (index - 1))
				{
					sb.append("<option value=\"").append((i + 1)).append("\" selected=\"selected\">").append((i + 1)).append("</option>");
				}
				else
				{
					sb.append("<option value=\"").append((i + 1)).append("\">").append((i + 1)).append("</option>");
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